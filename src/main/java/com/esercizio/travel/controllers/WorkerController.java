package com.esercizio.travel.controllers;

import com.esercizio.travel.entities.Worker;
import com.esercizio.travel.exceptions.ValidationException;
import com.esercizio.travel.payloads.WorkerDTO;
import com.esercizio.travel.payloads.WorkerResponseDTO;
import com.esercizio.travel.services.WorkerService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/workers")
public class WorkerController {
    private final WorkerService workerService;

    public WorkerController(WorkerService workerService) {
        this.workerService = workerService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public WorkerResponseDTO saveWorker(@RequestBody @Validated WorkerDTO body, BindingResult validResult) {
        checkValidation(validResult);
        Worker saved = workerService.save(body);
        return new WorkerResponseDTO(saved.getId()); // solo id, non serve tutto l'oggetto
    }

    @GetMapping
    public Page<Worker> getWorkers(@RequestParam(defaultValue = "0") int page,
                                    @RequestParam(defaultValue = "10") int size,
                                    @RequestParam(defaultValue = "lastName") String orderBy) {
        return workerService.getAll(page, size, orderBy);
    }

    @GetMapping("/{workerId}")
    public Worker getById(@PathVariable UUID workerId) {
        return workerService.findById(workerId);
    }

    @PutMapping("/{workerId}")
    public Worker updateWorker(@PathVariable UUID workerId, @RequestBody @Validated WorkerDTO body, BindingResult validResult) {
        checkValidation(validResult);
        return workerService.findByIdAndUpdate(workerId, body);
    }

    @DeleteMapping("/{workerId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteWorker(@PathVariable UUID workerId) {
        workerService.findByIdAndDelete(workerId);
    }

    // e' multipart non json quindi niente @RequestBody
    @PostMapping("/{workerId}/avatar")
    public Worker uploadAvatar(@PathVariable UUID workerId, @RequestParam("avatar") MultipartFile avatar) {
        return workerService.uploadAvatar(workerId, avatar);
    }

    private void checkValidation(BindingResult validResult) {
        if (validResult.hasErrors()) {
            List<String> errorsList = validResult.getFieldErrors().stream()
                    .map(fieldError -> fieldError.getDefaultMessage())
                    .toList();
            throw new ValidationException(errorsList);
        }
    }
}
