package com.budova.everhome.driver;

import com.budova.everhome.domain.*;
import com.budova.everhome.dto.ConnectionDto;
import com.budova.everhome.dto.SetTemperatureDto;
import com.budova.everhome.dto.TemperatureDto;
import com.budova.everhome.dto.ValvePosDto;
import com.budova.everhome.repos.ConnectionRepo;
import com.budova.everhome.repos.SetTemperatureRepo;
import com.budova.everhome.repos.TemperatureRepo;
import com.budova.everhome.repos.ValvePosRepo;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.exception.ModbusIOException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusNumberException;
import com.intelligt.modbus.jlibmodbus.exception.ModbusProtocolException;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;

@Controller
@EnableScheduling
public class RautControllerDriver {

    @Autowired
    private TemperatureRepo tempRepo;
    @Autowired
    private ValvePosRepo valvePosRepo;
    @Autowired
    private SetTemperatureRepo setTemperatureRepo;
    @Autowired
    private ConnectionRepo connectionRepo;

    @Autowired
    private SimpMessagingTemplate template;

    private final static String CONTROLLER_IP = "192.168.1.151";

    private final TcpParameters tcpParameters;
    private final ModbusMaster master;

    private RautControllerDriver() {
        tcpParameters = new TcpParameters();
        tcpParameters.setKeepAlive(true);
        tcpParameters.setPort(Modbus.TCP_PORT);
        try {
            tcpParameters.setHost(InetAddress.getByName(CONTROLLER_IP));
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
        Modbus.setAutoIncrementTransactionId(true);
        master = ModbusMasterFactory.createModbusMasterTCP(tcpParameters);
        master.setResponseTimeout(1000);
    }

    @Scheduled(fixedDelay = 1000L)
    public void poll() {
        LocalDateTime now = LocalDateTime.now();
        try {

            if (!master.isConnected()) {
                master.connect();
            }

            int[] regs = master.readHoldingRegisters(1, 0, 4);

            Connection c = new Connection(Parameter.RAUT_CONNECTION, now, true);
            ConnectionDto cDto = new ConnectionDto(c);
            template.convertAndSend("/topic/connection", cDto);
            Connection prevC = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
            if(prevC == null || Connection.isModuled(prevC, c)) {
                connectionRepo.save(c);
            }

            float t1Val = (float) regs[0] / 10;
            Temperature t1 = new Temperature(Parameter.TEMPERATURE_S1, now, t1Val);
            TemperatureDto t1Dto = new TemperatureDto(t1);
            template.convertAndSend("/topic/temperature1", t1Dto);
            Temperature prevT1 = tempRepo.findFirstByParamIsOrderByTimeDesc(Parameter.TEMPERATURE_S1);
            if (prevT1 == null || Temperature.isModuled(t1, prevT1)) {
                tempRepo.save(t1);
            }

            float t2Val = (float) regs[1] / 10;
            Temperature t2 = new Temperature(Parameter.TEMPERATURE_S2, now, t2Val);
            TemperatureDto t2Dto = new TemperatureDto(t2);
            template.convertAndSend("/topic/temperature2", t2Dto);
            Temperature prevT2 = tempRepo.findFirstByParamIsOrderByTimeDesc(Parameter.TEMPERATURE_S2);
            if (prevT2 == null || Temperature.isModuled(t2, prevT2)) {
                tempRepo.save(t2);
            }

            float stVal = (float) regs[2] / 10;
            SetTemperature st = new SetTemperature(Parameter.SET_TEMPERATURE, now, stVal);
            SetTemperatureDto stDto = new SetTemperatureDto(st);
            template.convertAndSend("/topic/set_temperature", stDto);
            SetTemperature prevSt = setTemperatureRepo.findFirstByParamIsOrderByTimeDesc(Parameter.SET_TEMPERATURE);
            if (prevSt == null || SetTemperature.isModuled(st, prevSt)) {
                setTemperatureRepo.save(st);
            }

            float vVal = regs[3] == 2 ? 1F : 0F;
            ValvePos v = new ValvePos(Parameter.VALVE_POSITION, now, vVal);
            ValvePosDto vDto = new ValvePosDto(v);
            template.convertAndSend("/topic/valve", vDto);
            ValvePos prevV = valvePosRepo.findFirstByParamIsOrderByTimeDesc(Parameter.VALVE_POSITION);
            if (prevV == null || ValvePos.isModuled(v, prevV)) {
                valvePosRepo.save(v);
            }
        } catch (ModbusProtocolException | ModbusNumberException | ModbusIOException e) {
            Connection c = new Connection(Parameter.RAUT_CONNECTION, now, false);
            ConnectionDto cDto = new ConnectionDto(c);
            template.convertAndSend("/topic/connection", cDto);
            Connection prevC = connectionRepo.findFirstByParamIsOrderByTimeDesc(Parameter.RAUT_CONNECTION);
            if(prevC == null || Connection.isModuled(prevC, c)) {
                connectionRepo.save(c);
            }
            System.err.println(e);        }
    }

    @MessageMapping("/setTemperature/inc")
    @SendTo("/topic/set_temperature")
    public SetTemperatureDto incSetTemperature(SetTemperatureDto stDto) throws Exception {
        stDto.setTime(LocalDateTime.now());
        stDto.setValue(stDto.getValue() + 1.0F);
        master.writeSingleRegister(1, 2, (int) stDto.getValue().floatValue() * 10);
        SetTemperature st = new SetTemperature(Parameter.SET_TEMPERATURE, LocalDateTime.now(), stDto.getValue());
        setTemperatureRepo.save(st);
        return stDto;
    }

    @MessageMapping("/setTemperature/dec")
    @SendTo("/topic/set_temperature")
    public SetTemperatureDto decSetTemperature(SetTemperatureDto stDto) throws Exception {
        stDto.setTime(LocalDateTime.now());
        stDto.setValue(stDto.getValue() - 1.0F);
        master.writeSingleRegister(1, 2, (int) stDto.getValue().floatValue() * 10);
        SetTemperature st = new SetTemperature(Parameter.SET_TEMPERATURE, stDto.getTime(), stDto.getValue());
        setTemperatureRepo.save(st);
        return stDto;
    }
}
