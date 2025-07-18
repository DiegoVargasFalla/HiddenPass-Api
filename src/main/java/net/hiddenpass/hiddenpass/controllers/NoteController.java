package net.hiddenpass.hiddenpass.controllers;

import jakarta.validation.Valid;
import net.hiddenpass.hiddenpass.models.NoteEntity;
import net.hiddenpass.hiddenpass.responseDTO.DeleteNoteDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;
import net.hiddenpass.hiddenpass.service.NoteService;
import net.hiddenpass.hiddenpass.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/v1")
public class NoteController {

    private final UserService userService;
    private final NoteService noteService;

    public NoteController(UserService userService, NoteService noteService) {
        this.userService = userService;
        this.noteService = noteService;
    }

    @PostMapping("/add-note")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Optional<NoteEntity>> CreateNote(@RequestBody NoteEntityDTO noteEntityDTO, @RequestHeader("Authorization") String token) throws Exception{
        String email = userService.getUsernameFromToken(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Optional<NoteEntity> noteDTO = noteService.createNote(email, noteEntityDTO);
        return ResponseEntity.status(HttpStatus.OK).body(noteDTO);
    }

    @PostMapping("/notes-user")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<NoteEntity>> getUserNotes(@RequestHeader("Authorization") String token) throws Exception{
        String email = userService.getUsernameFromToken(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(noteService.getAllNotes(email));
    }

    @PatchMapping("/update-note")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> updateNote(@RequestHeader("Authorization") String token, @Valid @RequestBody  NoteEntityDTO noteEntityDTO) throws Exception {
        String email = userService.getUsernameFromToken(token);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(noteService.updateNote(email, noteEntityDTO));
    }

    @DeleteMapping("/delete-note")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<?> deleteNote(@RequestHeader("Authorization") String token, @Valid @RequestBody DeleteNoteDTO deleteNoteDTO) throws Exception {
        String email = userService.getUsernameFromToken(token);
        System.out.println("-> token: " + token);
        System.out.println("-> email: " + email);

        if (email == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(noteService.deleteNote(email, deleteNoteDTO));
    }
}
