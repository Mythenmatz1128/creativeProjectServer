package controller;

import java.time.LocalDate;

import domain.FluctuationRate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import network.Packet;
import network.ProtocolType;
import network.protocolCode.RealEstateInfoCode;
import service.AverageDataService;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class RealEstateInfoController implements Controller{

    private final AverageDataService averageDataService;
    //1. 지역별 부동산 가격 제공 컨트롤러
    @Override
    public Packet process(Packet receivePacket) {
        log.info("RealEstateInfoController 입니다.");
        byte protocolCode = receivePacket.getProtocolCode();
        Packet packet = null;

        if(protocolCode == RealEstateInfoCode.SEND_DATA_REQ.getCode()){ // 기능 1.1
            packet = sendDataPacket(receivePacket);
        }
        else if(protocolCode == RealEstateInfoCode.SEND_GRAPH_DATA_REQ.getCode()){ //기능 1.2
            packet = sendGraphDataPacket(receivePacket);
        }
        else{
            throw new RuntimeException("존재하지 않는 코드입니다");
        }
        return packet;
    }

    private Packet sendDataPacket(Packet receivePacket) {
        log.info("기능 1.1 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte ProtocolCode = RealEstateInfoCode.SEND_DATA_RES.getCode();

        LocalDate now = LocalDate.now();
        int year = now.getYear();
        int month = now.getMonthValue()-1;

        List<FluctuationRate> list2 = averageDataService.findFluctuationLateByDate(year, month);


        Packet packet = new Packet(protocolType,ProtocolCode,list2);
        return packet;
    }

    private Packet sendGraphDataPacket(Packet receivePacket) {
        log.info("기능 1.2 실행");
        byte protocolType = ProtocolType.REAL_ESTATE_INFO.getType();
        byte ProtocolCode = RealEstateInfoCode.SEND_GRAPH_DATA_RES.getCode();
        String body = "1.2 SEND_GRAPH_DATA_RES";

        Packet packet = new Packet(protocolType,ProtocolCode,body);
        return packet;
    }
}
