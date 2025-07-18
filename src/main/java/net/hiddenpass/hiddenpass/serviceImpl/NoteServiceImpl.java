package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.models.NoteEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.repository.NoteRepository;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import net.hiddenpass.hiddenpass.responseDTO.DeleteNoteDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;
import net.hiddenpass.hiddenpass.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Iterator;
import java.util.List;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;

    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
    }

    /**
     *
     * @param email email that identify user
     * @param noteDTO DTO note
     * @return noteEntity already create
     */
    @Override
    @Transactional
    public Optional<NoteEntity> createNote(String email, NoteEntityDTO noteDTO)  {

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);
        if (userExisting.isPresent()) {
            UserEntity user = userExisting.get();
            NoteEntity noteEntity = new NoteEntity();

            noteEntity.setTitle(noteDTO.getTitle());
            noteEntity.setContent(noteDTO.getContent());
            noteEntity.setZoneDateTimeClient(noteDTO.getZoneDateClient());
            noteEntity.setDateTimeClient(noteDTO.getIsoDate(), noteDTO.getZoneDateClient());
            noteEntity.setUser(user);

            NoteEntity savedNoteEntity = noteRepository.save(noteEntity);

            return Optional.of(savedNoteEntity);
        }
        throw new IllegalArgumentException("The user does not exist");
    }

    @Override
    public List<NoteEntity> getAllNotes(String email)  {

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);
        if (userExisting.isPresent()) {

            UserEntity user = userExisting.get();
            return user.getListNotes();
        }
        throw new IllegalArgumentException("The user does not exist");
    }

    @Override
    public NoteEntityDTO updateNote(String email, NoteEntityDTO noteEntityDTO) {

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);
        if (userExisting.isPresent()) {
            UserEntity user = userExisting.get();

            for(NoteEntity note: user.getListNotes()) {

                if(note.getId().equals(noteEntityDTO.getId())) {

                    if(noteEntityDTO.getTitle() != null) {
                        note.setTitle(noteEntityDTO.getTitle());

                    } if(noteEntityDTO.getContent() != null) {
                        note.setContent(noteEntityDTO.getContent());
                    }
                    userRepository.save(user);
                    return noteEntityDTO;
                }
            }
        }
        throw new IllegalArgumentException("The user does not exist");
    }

    @Override
    public boolean deleteNote(String email, DeleteNoteDTO deleteNoteDTO) {
        System.out.println(deleteNoteDTO.getNoteId());

        Optional<UserEntity> userEntityOptional = userRepository.findByUsername(email);

        if (userEntityOptional.isPresent()) {
            UserEntity user = userEntityOptional.get();
            Iterator<NoteEntity> iterator = user.getListNotes().iterator();
            while (iterator.hasNext()) {
                NoteEntity noteEntity = iterator.next();
                if (noteEntity.getId().equals(deleteNoteDTO.getNoteId())) {
                    System.out.println("Es igual el id" + deleteNoteDTO.getNoteId() + " ---- " + noteEntity.getId());
                    iterator.remove();
                    userRepository.save(user);
                    return true;
                }
            }
            throw new IllegalArgumentException("The note does not exist");
        }
        throw new IllegalArgumentException("The user does not exist");
    }
}
