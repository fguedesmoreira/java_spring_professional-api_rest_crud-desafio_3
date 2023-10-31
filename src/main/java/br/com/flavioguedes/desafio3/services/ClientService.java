package br.com.flavioguedes.desafio3.services;

import java.util.Optional;

import org.hibernate.exception.ConstraintViolationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.com.flavioguedes.desafio3.dto.ClientDTO;
import br.com.flavioguedes.desafio3.entities.Client;
import br.com.flavioguedes.desafio3.exceptions.DataBaseException;
import br.com.flavioguedes.desafio3.exceptions.ResourceNotFoundException;
import br.com.flavioguedes.desafio3.repositories.ClientRepository;
import jakarta.persistence.EntityNotFoundException;

@Service
public class ClientService {

	@Autowired
	private ClientRepository repository;

	@Transactional(readOnly = true)
	public ClientDTO findById(Long id) {
		Optional<Client> result = repository.findById(id);
		Client client = result.orElseThrow(() -> new ResourceNotFoundException("Cliente não encontrado."));
		return new ClientDTO(client);
	}

	@Transactional(readOnly = true)
	public Page<ClientDTO> findAll(Pageable pageable) {
		Page<Client> result = repository.findAll(pageable);
		return result.map(x -> new ClientDTO(x));
	}

	@Transactional
	public ClientDTO insert(ClientDTO dto) {
		try {
			Client entity = new Client();
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referencial.");
		} catch (ConstraintViolationException e) {
			throw new DataBaseException("Violação de constraint.");
		}
	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public ClientDTO update(Long id, ClientDTO dto) {
		try {
			Client entity = repository.getReferenceById(id);
			copyDtoToEntity(dto, entity);
			entity = repository.save(entity);
			return new ClientDTO(entity);
		} catch (EntityNotFoundException e) {
			throw new ResourceNotFoundException("Cliente não encontrado.");
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referencial.");
		} catch (ConstraintViolationException e) {
			throw new DataBaseException("Violação de constraint.");
		}
		
		
		

	}

	@Transactional(propagation = Propagation.SUPPORTS)
	public void delete(Long id) {

		if (!repository.existsById(id)) {
			throw new ResourceNotFoundException("Cliente não encontrado.");
		}

		try {
			repository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataBaseException("Falha de integridade referencial.");
		}

	}

	private void copyDtoToEntity(ClientDTO dto, Client entity) {
		entity.setName(dto.getName());
		entity.setCpf(dto.getCpf());
		entity.setIncome(dto.getIncome());
		entity.setBirthDate(dto.getBirthDate());
		entity.setChildren(dto.getChildren());
	}

}