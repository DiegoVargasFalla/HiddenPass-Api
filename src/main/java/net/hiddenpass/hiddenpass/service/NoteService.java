package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.models.NoteEntity;
import net.hiddenpass.hiddenpass.responseDTO.DeleteNoteDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;

import java.util.List;
import java.util.Optional;

public interface NoteService {
    Optional<NoteEntity> createNote(String email, NoteEntityDTO noteEntityDTO) throws Exception;
    List<NoteEntity> getAllNotes(String email) throws Exception;
    NoteEntityDTO updateNote(String email, NoteEntityDTO noteEntityDTO) throws Exception;
    boolean deleteNote(String email, DeleteNoteDTO deleteNoteDTO);
//    NoteEntity getNote(int id);
//    void deleteNote(NoteEntity note);
//    NoteEntity updateNote(NoteEntity note);
}
