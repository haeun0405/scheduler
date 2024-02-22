package org.sparta.scheduler;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;
import org.junit.jupiter.api.Test;
import org.sparta.scheduler.Dto.CommentDTO;
import org.sparta.scheduler.Dto.LoginRequestDto;
import org.sparta.scheduler.Dto.SignupRequestDto;
import org.sparta.scheduler.Dto.TaskDTO;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
class SchedulerApplicationTests {

    @Test
    void signupDtoTest() {
        // Given
        String expectedUsername = "testUser";
        String expectedPassword = "testPass";
        SignupRequestDto signupDto = new SignupRequestDto();
        signupDto.setUsername(expectedUsername);
        signupDto.setPassword(expectedPassword);

        // When
        String actualUsername = signupDto.getUsername();
        String actualPassword = signupDto.getPassword();

        // Then
        assertEquals(expectedUsername, actualUsername);
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void loginDtoTest() {
        // Given
        String expectedUsername = "testUser";
        String expectedPassword = "testPass";
        LoginRequestDto loginDto = new LoginRequestDto(expectedUsername, expectedPassword);

        // When
        String actualUsername = loginDto.getUsername();
        String actualPassword = loginDto.getPassword();

        // Then
        assertEquals(expectedUsername, actualUsername);
        assertEquals(expectedPassword, actualPassword);
    }

    @Test
    void commentDtoTest() {
        // Given
        Long expectedId = 1L;
        String expectedContent = "This is a test comment";
        LocalDateTime expectedDate = LocalDateTime.now();
        CommentDTO commentDto = new CommentDTO();
        commentDto.setId(expectedId);
        commentDto.setContent(expectedContent);
        commentDto.setCreatedDate(expectedDate);

        // When
        Long actualId = commentDto.getId();
        String actualContent = commentDto.getContent();
        LocalDateTime actualDate = commentDto.getCreatedDate();

        // Then
        assertEquals(expectedId, actualId);
        assertEquals(expectedContent, actualContent);
        assertEquals(expectedDate, actualDate);
    }

    @Test
    void taskDtoTest() {
        // Given
        Long expectedId = 1L;
        String expectedTitle = "Task Title";
        String expectedContents = "Task Content";
        String expectedAssignee = "Assignee Name";
        LocalDateTime expectedDate = LocalDateTime.now();
        TaskDTO taskDto = new TaskDTO(expectedId, expectedTitle, expectedContents, expectedAssignee, expectedDate);

        // When
        Long actualId = taskDto.getId();
        String actualTitle = taskDto.getTitle();
        String actualContents = taskDto.getContents();
        String actualAssignee = taskDto.getAssignee();

        // Then
        assertEquals(expectedId, actualId);
        assertEquals(expectedTitle, actualTitle);
        assertEquals(expectedContents, actualContents);
        assertEquals(expectedAssignee, actualAssignee);
    }

}
