package com.budova.everhome.driver;

import com.budova.everhome.domain.Parameter;
import com.budova.everhome.domain.SetTemperature;
import com.budova.everhome.domain.Temperature;
import com.budova.everhome.domain.ValvePos;
import com.budova.everhome.dto.SetTemperatureDto;
import com.budova.everhome.dto.TemperatureDto;
import com.budova.everhome.dto.ValvePosDto;
import com.budova.everhome.repos.SetTemperatureRepo;
import com.budova.everhome.repos.TemperatureRepo;
import com.budova.everhome.repos.ValvePosRepo;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import org.springframework.beans.factory.annotation.Autowired;
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
    private SimpMessagingTemplate template;

    private final static String CONTROLLER_IP = "192.168.0.228";

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
        master.setResponseTimeout(3000);
    }

    @Scheduled(fixedDelay = 1000L)
    public void poll() {
        try {
            if (!master.isConnected()) {
                master.connect();
            }
            int[] regs = master.readHoldingRegisters(1, 0, 4);
            LocalDateTime now = LocalDateTime.now();

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
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
