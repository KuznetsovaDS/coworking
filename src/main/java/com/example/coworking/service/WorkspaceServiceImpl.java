package com.example.coworking.service;

import com.example.coworking.exceptions.CustomException;
import com.example.coworking.model.dto.request.WorkspaceRequestDto;
import com.example.coworking.model.dto.response.BookingResponseDto;
import com.example.coworking.model.dto.response.WorkspaceResponseDto;
import com.example.coworking.model.entity.Workspace;
import com.example.coworking.model.enums.BookingStatus;
import com.example.coworking.model.enums.WorkspaceStatus;
import com.example.coworking.model.repositories.WorkspaceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import static com.example.coworking.utils.PaginationUtil.getPageRequest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class WorkspaceServiceImpl implements WorkspaceService{
    private final ObjectMapper mapper;
    private final WorkspaceRepo workspaceRepo;

    @Override
    public WorkspaceResponseDto createWorkspace(WorkspaceRequestDto workspaceRequestDto) {
        Workspace workspace = mapper.convertValue(workspaceRequestDto, Workspace.class);
        workspace.setCreatedAt(LocalDateTime.now());
        Workspace save = workspaceRepo.save(workspace);
        return mapper.convertValue(save, WorkspaceResponseDto.class);
    }

    @Override
    public WorkspaceResponseDto getWorkspace(Long id) {
        Workspace workspace = getWorkspaceById(id);
        return mapper.convertValue(workspace, WorkspaceResponseDto.class);
    }

    private Workspace getWorkspaceById(Long id) {
        return workspaceRepo.findById(id)
                .orElseThrow(()->new CustomException("No workspace here!", HttpStatus.NOT_FOUND));
    }

    @Override
    public WorkspaceResponseDto update(Long id, WorkspaceRequestDto workspaceRequestDto) {
        Workspace workspace = getWorkspaceById(id);

        workspace.setName(StringUtils.isBlank(workspaceRequestDto.getName()) ? workspace.getName() : workspaceRequestDto.getName());
        workspace.setDescription(StringUtils.isBlank(workspaceRequestDto.getDescription()) ? workspace.getDescription() : workspaceRequestDto.getDescription());
        if (workspaceRequestDto.getPrice() != null) {
            workspace.setPrice(workspaceRequestDto.getPrice());
        }
        if (workspaceRequestDto.getCapacity() != null) {
            workspace.setCapacity(workspaceRequestDto.getCapacity());
        }
        workspace.setStatus(WorkspaceStatus.UPDATED);
        Workspace save = workspaceRepo.save(workspace);
        return mapper.convertValue(save, WorkspaceResponseDto.class);
    }
    @Override
    public void deleteWorkspace(Long id) {
        Workspace workspace = getWorkspaceById(id);
        workspace.setStatus(WorkspaceStatus.DELETED);
        workspaceRepo.save(workspace);
    }

    @Override
    public Page<WorkspaceResponseDto> getAllAvailableWorkspaces(Integer page, Integer perPage, String sort, Sort.Direction order,
                                                                 LocalDateTime startTime,LocalDateTime endTime, BookingStatus status) {
        Pageable pageRequest = getPageRequest(page, perPage, sort, order);

        Page<Workspace> pageList = workspaceRepo.findAllAvailable(pageRequest, status, startTime, endTime);

        List<WorkspaceResponseDto> response = pageList.getContent().stream()
                .map(workspace -> {
                    WorkspaceResponseDto workspaceresponseDto = mapper.convertValue(workspace, WorkspaceResponseDto.class);
                    List<BookingResponseDto> futureBookings = workspace.getBookings().stream()
                            .map(booking -> mapper.convertValue(booking, BookingResponseDto.class))
                            .collect(Collectors.toList());
                    workspaceresponseDto.setBookings(futureBookings);
                    return workspaceresponseDto;})
                .collect(Collectors.toList());
        return new PageImpl<>(response);
    }
}