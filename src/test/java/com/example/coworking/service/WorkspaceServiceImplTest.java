package com.example.coworking.service;

import com.example.coworking.model.dto.request.WorkspaceRequestDto;
import com.example.coworking.model.dto.response.WorkspaceResponseDto;
import com.example.coworking.model.entity.Workspace;
import com.example.coworking.model.enums.BookingStatus;
import com.example.coworking.model.enums.WorkspaceStatus;
import com.example.coworking.model.repositories.WorkspaceRepo;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.data.domain.*;

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.Assert.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WorkspaceServiceImplTest {
    @InjectMocks
    private WorkspaceServiceImpl workspaceService;

    @Mock
    private WorkspaceRepo workspaceRepo;

    @Spy
    private ObjectMapper mapper;

    @Test
    public void createWorkspace() {
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto();
        workspaceRequestDto.setName("New Workspace");

        Workspace workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("New Workspace");

        when(workspaceRepo.save(any(Workspace.class))).thenReturn(workspace);

        WorkspaceResponseDto result = workspaceService.createWorkspace(workspaceRequestDto);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("New Workspace", result.getName());
    }

    @Test
    public void getWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("First Workspace");

        when(workspaceRepo.findById(anyLong())).thenReturn(Optional.of(workspace));

        WorkspaceResponseDto result = workspaceService.getWorkspace(1L);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("First Workspace", result.getName());
    }
    @Test
    public void update() {
        WorkspaceRequestDto workspaceRequestDto = new WorkspaceRequestDto();
        workspaceRequestDto.setName("Updated name");
        workspaceRequestDto.setDescription("Updated description");
        workspaceRequestDto.setPrice(2000.0);
        workspaceRequestDto.setCapacity(20);

        Workspace workspace = new Workspace();
        workspace.setId(1L);
        workspace.setName("Old Workspace");
        workspace.setDescription("Old description");
        workspace.setPrice(1000.0);
        workspace.setCapacity(10);
        workspace.setStatus(WorkspaceStatus.UPDATED);

        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        when(workspaceRepo.save(any(Workspace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        WorkspaceResponseDto result = workspaceService.update(1L, workspaceRequestDto);

        assertNotNull(result);
        assertEquals(Long.valueOf(1L), result.getId());
        assertEquals("Updated name", result.getName());
        assertEquals("Updated description", result.getDescription());
        assertEquals(Double.valueOf(2000.0), result.getPrice());
        assertEquals(Integer.valueOf(20), result.getCapacity());

        verify(workspaceRepo, times(1)).save(any(Workspace.class));
    }

    @Test
    public void deleteWorkspace() {
        Workspace workspace = new Workspace();
        workspace.setId(1L);
        workspace.setStatus(WorkspaceStatus.UPDATED);
        when(workspaceRepo.findById(1L)).thenReturn(Optional.of(workspace));
        workspaceService.deleteWorkspace(1L);
        assertEquals(WorkspaceStatus.DELETED, workspace.getStatus());
        verify(workspaceRepo, times(1)).save(workspace);
    }
}