package com.example.coworking.controllers;

import com.example.coworking.model.dto.request.WorkspaceRequestDto;
import com.example.coworking.model.dto.response.WorkspaceResponseDto;
import com.example.coworking.model.enums.BookingStatus;
import com.example.coworking.service.WorkspaceService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Tag(name = "Workspaces")
@RestController
@RequestMapping("/workspaces")
@RequiredArgsConstructor
public class WorkspaceController {

    private final WorkspaceService workspaceService;

    @PostMapping
    @Operation(summary =  "Create workspace")
    public WorkspaceResponseDto createCar(@RequestBody WorkspaceRequestDto workspaceRequestDto){
        return workspaceService.createWorkspace(workspaceRequestDto);

    }
    @GetMapping("/{id}")
    @Operation(summary =  "Get workspace")
    public WorkspaceResponseDto readWorkspace(@PathVariable Long id){
        return workspaceService.getWorkspace(id);

    }
    @PutMapping("/{id}")
    @Operation(summary =  "Update workspace")
    public WorkspaceResponseDto updateWorkspace(@PathVariable Long id, @RequestBody WorkspaceRequestDto workspaceRequestDto){
        return workspaceService.update(id, workspaceRequestDto);
    }
    @DeleteMapping("/{id}")
    @Operation(summary =  "Delete workspace")
    public void deleteWorkspace(@PathVariable Long id){
        workspaceService.deleteWorkspace(id);

    }
    @GetMapping("/all")
    @Operation(summary =  "Get all workspace")
    public Page<WorkspaceResponseDto> getAllAvailableWorkspace(
                                        @RequestParam(defaultValue = "1") Integer page,
                                        @RequestParam(defaultValue = "10")  Integer perPage,
                                        @RequestParam(defaultValue = "name")  String sort,
                                        @RequestParam(defaultValue = "ASC")  Sort.Direction order,
                                         @RequestParam(required = false) LocalDateTime startTime,
                                        @RequestParam(required = false) LocalDateTime endTime,
                                         @RequestParam(required = false) BookingStatus status){
        return workspaceService.getAllAvailableWorkspaces(page, perPage, sort, order, startTime, endTime, status);
    }
}
