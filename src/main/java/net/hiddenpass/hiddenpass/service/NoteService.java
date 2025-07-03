package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.models.NoteEntity;
import net.hiddenpass.hiddenpass.responseDTO.DeleteNoteDTO;
import net.hiddenpass.hiddenpass.responseDTO.MasterKeyDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityUpdateDTO;

import java.util.ArrayList;
import java.util.Optional;

public interface NoteService {
    Optional<NoteEntity> createNote(String email, NoteEntityDTO noteEntityDTO) throws Exception;
    ArrayList<NoteEntity> getAllNotes(String email, MasterKeyDTO masterKeyDTO) throws Exception;
    NoteEntityUpdateDTO updateNote(String email, NoteEntityUpdateDTO noteEntityUpdateDTODTO) throws Exception;
    boolean deleteNote(String email, DeleteNoteDTO deleteNoteDTO);
//    NoteEntity getNote(int id);
//    void deleteNote(NoteEntity note);
//    NoteEntity updateNote(NoteEntity note);
}
