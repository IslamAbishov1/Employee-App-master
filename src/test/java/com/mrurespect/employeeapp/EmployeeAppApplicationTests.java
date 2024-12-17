package com.mrurespect.employeeapp;

import com.mrurespect.employeeapp.dao.EmployeeRepository;
import com.mrurespect.employeeapp.entity.Employee;
import com.mrurespect.employeeapp.service.EmployeeServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmployeeAppApplicationTests {

    private EmployeeRepository employeeRepository;
    private EmployeeServiceImpl employeeService;

    @BeforeEach
    void setUp() {
        employeeRepository = Mockito.mock(EmployeeRepository.class);
        employeeService = new EmployeeServiceImpl(employeeRepository);
    }

    @Test
    void testFindAll() {
        Employee emp1 = new Employee("John", "Doe", "john@examle.com");
        Employee emp2 = new Employee("Jane", "Smith", "jane@example.com");
        when(employeeRepository.findAll()).thenReturn(Arrays.asList(emp1, emp2));

        List<Employee> result = employeeService.findAll();
        assertEquals(2, result.size());
        assertEquals("John", result.get(0).getFirstName());
        assertEquals("Jane", result.get(1).getFirstName());
    }

    @Test
    void testFindById() {
        Employee emp = new Employee("Test", "User", "test@example.com");
        emp.setId(10);
        when(employeeRepository.findById(10)).thenReturn(Optional.of(emp));

        Employee found = employeeService.findById(10);
        assertNotNull(found);
        assertEquals(10, found.getId());
    }

    @Test
    void testFindByIdNotFound() {
        when(employeeRepository.findById(999)).thenReturn(Optional.empty());
        RuntimeException ex = assertThrows(RuntimeException.class,
                () -> employeeService.findById(999));
        assertTrue(ex.getMessage().contains("Did not find employee id - 999"));
    }

    @Test
    void testSave() {
        Employee emp = new Employee("Save", "User", "save@example.com");
        when(employeeRepository.save(emp)).thenReturn(emp);

        Employee saved = employeeService.save(emp);
        assertEquals("Save", saved.getFirstName());
        verify(employeeRepository, times(1)).save(emp);
    }

    @Test
    void testDeleteById() {
        employeeService.deleteById(5);
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        verify(employeeRepository, times(1)).deleteById(captor.capture());
        assertEquals(5, captor.getValue());
    }
}
