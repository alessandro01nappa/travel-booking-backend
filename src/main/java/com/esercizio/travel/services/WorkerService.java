package com.esercizio.travel.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.exceptions.BadRequestException;
import com.esercizio.travel.exceptions.NotFoundException;
import com.esercizio.travel.payloads.WorkerDTO;
import com.esercizio.travel.repositories.WorkerRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
public class WorkerService {
    private final WorkerRepository workerRepository;
    private final Cloudinary cloudinary;

    public WorkerService(WorkerRepository workerRepository, Cloudinary cloudinary) {
        this.workerRepository = workerRepository;
        this.cloudinary = cloudinary;
    }

    public Worker save(WorkerDTO body) {
        if (workerRepository.existsByEmail(body.email())) { // no email doppie
            throw new BadRequestException("L'email " + body.email() + " e' gia' in uso");
        }
        Worker newWorker = new Worker(body.username(), body.firstName(), body.lastName(), body.email());
        return workerRepository.save(newWorker);
    }

    public Worker findById(UUID id) {
        return workerRepository.findById(id).orElseThrow(() -> new NotFoundException(id));
    }

    public Page<Worker> getAll(int page, int size, String orderBy) {
        // size max 15, cosi non si scarica tutto in una volta
        if (size <= 0) size = 10;
        if (size > 15) size = 15;
        if (page < 0) page = 0;
        Pageable pageable = PageRequest.of(page, size, Sort.by(orderBy));
        return workerRepository.findAll(pageable);
    }

    public Worker findByIdAndUpdate(UUID id, WorkerDTO body) {
        Worker found = findById(id);

        if (!found.getEmail().equals(body.email()) && workerRepository.existsByEmail(body.email())) {
            throw new BadRequestException("L'email " + body.email() + " e' gia' in uso");
        }

        found.setUsername(body.username());
        found.setFirstName(body.firstName());
        found.setLastName(body.lastName());
        found.setEmail(body.email());

        return workerRepository.save(found);
    }

    public void findByIdAndDelete(UUID id) {
        Worker found = findById(id);
        workerRepository.delete(found);
    }

    public Worker uploadAvatar(UUID id, MultipartFile file) {
        Worker found = findById(id);
        try {
            // manda su cloudinary e prende l'url della foto
            Map uploadResult = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            String url = (String) uploadResult.get("url");
            found.setProfileImageUrl(url);
            return workerRepository.save(found);
        } catch (IOException e) {
            throw new BadRequestException("Errore durante l'upload dell'immagine");
        }
    }
}
