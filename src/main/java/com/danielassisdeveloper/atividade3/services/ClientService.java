package com.danielassisdeveloper.atividade3.services;

import com.danielassisdeveloper.atividade3.dto.ClientDTO;
import com.danielassisdeveloper.atividade3.entities.Client;
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
public class ClientService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Transactional(readOnly = true)
    public Page<ClientDTO> findAll(Pageable pageable) {
        try {
            return clienteRepository.findAll(pageable).map(ClientDTO::new);
        } catch (RuntimeException e) {
            throw new ResourceNotFoundException("Não foi possível carregar os produtos.");
        }
    }

    @Transactional(readOnly = true)
    public ClientDTO findById(Long id) {
        return new ClientDTO(clienteRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recurso não encontrado.")));
    }

    @Transactional
    public ClientDTO insert(ClientDTO clientDTO) {
        Client client = new Client();
        copyDTOToEntity(clientDTO, client);
        client = clienteRepository.save(client);
        return new ClientDTO(client);
    }

    @Transactional
    public ClientDTO update(Long id, ClientDTO clientDTO) {
        try {
            Client client = new Client();
            copyDTOToEntity(clientDTO, client);
            client = clienteRepository.save(client);
            return new ClientDTO(client);
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

    private void copyDTOToEntity(ClientDTO clientDTO, Client client) {
        client.setId(clientDTO.getId());
        client.setName(clientDTO.getName());
        client.setCpf(clientDTO.getCpf());
        client.setIncome(clientDTO.getIncome());
        client.setBirthDate(clientDTO.getBirthDate());
        client.setChildren(clientDTO.getChildren());
    }
}
