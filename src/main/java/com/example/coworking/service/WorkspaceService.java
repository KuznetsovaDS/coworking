package com.example.coworking.service;

import com.example.coworking.model.dto.request.WorkspaceRequestDto;
import com.example.coworking.model.dto.response.WorkspaceResponseDto;
import com.example.coworking.model.enums.BookingStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.time.LocalDateTime;

public interface WorkspaceService {

    WorkspaceResponseDto createWorkspace(WorkspaceRequestDto workspaceRequestDto);

    WorkspaceResponseDto getWorkspace(Long id);

    WorkspaceResponseDto update(Long id, WorkspaceRequestDto workspaceRequestDto);

    void deleteWorkspace(Long id);

    Page<WorkspaceResponseDto> getAllAvailableWorkspaces(Integer page, Integer perPage, String sort, Sort.Direction order,
                                                LocalDateTime startTime,LocalDateTime endTime, BookingStatus status);
}
