package com.danielassisdeveloper.atividade3.services;

import com.danielassisdeveloper.atividade3.dto.ClienteDTO;
import com.danielassisdeveloper.atividade3.entities.Cliente;
import com.danielassisdeveloper.atividade3.repositories.ClienteRepository;
import com.danielassisdeveloper.atividade3.services.exceptions.DatabaseException;
import com.danielassisdeveloper.atividade3.services.exceptions.ResourceNotFoundException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public Page<ClienteDTO> findAll(Pageable pageable) {
        try {
            return clienteRepository.findAll(pageable).map(ClienteDTO::new);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Não foi possível carregar os produtos.");
        }
    }

    @Transactional(readOnly = true)
    public ClienteDTO findById(Long id) {
        return new ClienteDTO(clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado.")));
    }

    @Transactional
    public ClienteDTO insert(ClienteDTO clienteDTO) {
        Cliente cliente = new Cliente();
        copyDTOToEntity(clienteDTO, cliente);
        cliente = clienteRepository.save(cliente);
        return new ClienteDTO(cliente);
    }

    @Transactional
    public ClienteDTO update(Long id, ClienteDTO clienteDTO) {
        try {
            Cliente cliente = new Cliente();
            copyDTOToEntity(clienteDTO, cliente);
            cliente = clienteRepository.save(cliente);
            return new ClienteDTO(cliente);
        } catch (EntityNotFoundException e) {
            throw new ResourceNotFoundException("Recurso não encontrado.");
        }
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public void delete(Long id) {
        if (!clienteRepository.existsById(id)) {
            throw new ResourceNotFoundException("Recurso não encontrado.");
        }
        try {
            clienteRepository.deleteById(id);
        } catch (DataIntegrityViolationException e) {
            throw new DatabaseException("Falha de integridade referencial.");
        }
    }

    private void copyDTOToEntity(ClienteDTO clienteDTO, Cliente cliente) {
        cliente.setId(clienteDTO.getId());
        cliente.setName(clienteDTO.getName());
        cliente.setCpf(clienteDTO.getCpf());
        cliente.setIncome(clienteDTO.getIncome());
        cliente.setBirthDate(clienteDTO.getBirthDate());
        cliente.setChildren(clienteDTO.getChildren());
    }
}
