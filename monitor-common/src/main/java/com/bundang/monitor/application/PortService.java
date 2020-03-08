package com.bundang.monitor.application;

import com.bundang.monitor.domain.Port;
import com.bundang.monitor.domain.PortNotFoundException;
import com.bundang.monitor.domain.PortRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class PortService {

    private PortRepository portRepository;

    @Autowired
    public PortService(PortRepository portRepository) {
        this.portRepository = portRepository;
    }

    public List<Port> getPorts() {
        List<Port> ports = portRepository.findAll();

        return ports;
    }

    public List<Port> getPorts(Integer number) {
        List<Port> ports =
                portRepository.findAllByNumber(number);
        return ports;
    }

    public List<Port> getPorts(String state) {
        List<Port> ports =
                portRepository.findAllByState(state);
        return ports;
    }

    public Port getPort(Long id) {
        Port port = portRepository.findById(id)
                .orElseThrow(() -> new PortNotFoundException(id));
        return port;
    }

    public Port addPort(Port port) {
        return portRepository.save(port);
    }

    public void bulkCreate(List<Port> ports) {
        for (Port port : ports) {
            addPort(port);
        }
    }

    public Port updatePort(Long id, Integer number,
                           String state, LocalDateTime date) {
        Port port = portRepository.findById(id)
                .orElseThrow(() -> new PortNotFoundException(id));

        port.setNumber(number);
        port.setState(state);
        port.setDate(date);

        return port;
    }

    public Port deactivePort(Long id) {
        Port port = portRepository.findById(id)
                .orElseThrow(() -> new PortNotFoundException(id));

        port.deativate();

        return port;
    }
}
