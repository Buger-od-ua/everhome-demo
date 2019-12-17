package com.budova.everhome.driver;

import com.budova.everhome.domain.Temperature;
import com.budova.everhome.dto.TemperatureDto;
import com.budova.everhome.repos.TemperatureRepo;
import com.intelligt.modbus.jlibmodbus.Modbus;
import com.intelligt.modbus.jlibmodbus.master.ModbusMaster;
import com.intelligt.modbus.jlibmodbus.master.ModbusMasterFactory;
import com.intelligt.modbus.jlibmodbus.tcp.TcpParameters;
import org.springframework.beans.factory.annotation.Autowired;
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
    @SendTo("/topic/temperature")
    public void poll() {
        try {
            if (!master.isConnected()) {
                master.connect();
            }
            int[] regs = master.readHoldingRegisters(1, 0, 4);
            Temperature t1 = new Temperature(LocalDateTime.now(), (float) regs[0] / 10);
            Temperature prevT1 = tempRepo.findFirstByOrderByTimeDesc();
            if (prevT1 == null || Temperature.isModuled(t1, prevT1, 0.5F)) {
                tempRepo.save(t1);
            }
            TemperatureDto t1Dto = new TemperatureDto(t1);
            template.convertAndSend("/topic/temperature", t1Dto);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
