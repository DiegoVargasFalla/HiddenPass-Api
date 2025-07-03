package net.hiddenpass.hiddenpass.serviceImpl;

import net.hiddenpass.hiddenpass.models.NoteEntity;
import net.hiddenpass.hiddenpass.models.UserEntity;
import net.hiddenpass.hiddenpass.repository.NoteRepository;
import net.hiddenpass.hiddenpass.repository.UserRepository;
import net.hiddenpass.hiddenpass.responseDTO.DeleteNoteDTO;
import net.hiddenpass.hiddenpass.responseDTO.MasterKeyDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityDTO;
import net.hiddenpass.hiddenpass.responseDTO.NoteEntityUpdateDTO;
import net.hiddenpass.hiddenpass.service.EncryptionUtilsService;
import net.hiddenpass.hiddenpass.service.KeyStoreService;
import net.hiddenpass.hiddenpass.service.NoteService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Optional;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final EncryptionUtilsService encryptionUtils;
    private final KeyStoreService keyStoreService;

    public NoteServiceImpl(NoteRepository noteRepository, UserRepository userRepository, EncryptionUtilsService encryptionUtils, KeyStoreService keyStoreService) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.encryptionUtils = encryptionUtils;
        this.keyStoreService = keyStoreService;
    }

    /**
     *
     * @param email email that identify user
     * @param noteDTO DTO note
     * @return noteEntity already create
     * @throws Exception if user not exist
     */
    @Override
    @Transactional
    public Optional<NoteEntity> createNote(String email, NoteEntityDTO noteDTO) throws Exception {

        NoteEntityDTO decryptedNoteDTO = keyStoreService.decryptAllDataNote(noteDTO);

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);
        if (userExisting.isPresent()) {
            UserEntity user = userExisting.get();

            byte[] saltUser = user.getUserSalt();
            byte[] ivUser = user.getUserIv();

            SecretKey derivedKey = encryptionUtils.deriveKey(decryptedNoteDTO.getMasterKey(), saltUser);

            String encryptTitle = encryptionUtils.encrypt(decryptedNoteDTO.getTitle(), derivedKey, ivUser);
            String encryptContent = encryptionUtils.encrypt(decryptedNoteDTO.getContent(), derivedKey, ivUser);

            NoteEntity noteEntity = new NoteEntity();

            noteEntity.setTitle(encryptTitle);
            noteEntity.setContent(encryptContent);
            noteEntity.setZoneDateTimeClient(noteDTO.getZoneDateClient());
            noteEntity.setDateTimeClient(noteDTO.getIsoDate(), noteDTO.getZoneDateClient());
            noteEntity.setUser(user);

            NoteEntity savedNoteEntity = noteRepository.save(noteEntity);

            return Optional.of(savedNoteEntity);
        }
        throw new IllegalArgumentException("The user does not exist");
    }

    @Override
    public ArrayList<NoteEntity> getAllNotes(String email, MasterKeyDTO masterKeyDTO) throws Exception {

        byte[] decryptedAesKeyFront = keyStoreService.decryptAES(masterKeyDTO.getAesKey());
        byte[] ivFront = keyStoreService.exportBase64ToArray(masterKeyDTO.getIvFront());
        byte[] encryptedMasterKey = keyStoreService.exportBase64ToArray(masterKeyDTO.getMasterKey());

        String masterKey = keyStoreService.decryptMasterKey(encryptedMasterKey);

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);
        ArrayList<NoteEntity> noteEntityArrayList = new ArrayList<>();

        if (userExisting.isPresent()) {

            UserEntity user = userExisting.get();

            byte[] salt = user.getUserSalt();
            byte[] iv = user.getUserIv();

            SecretKey derivedKey = encryptionUtils.deriveKey(masterKey, salt);

             for(NoteEntity note: user.getListNotes()) {

                 String title = encryptionUtils.decrypt(note.getTitle(), derivedKey, iv);
                 String content = encryptionUtils.decrypt(note.getContent(), derivedKey, iv);

                 note.setTitle(keyStoreService.encryptDataWithAES(title, decryptedAesKeyFront, ivFront));
                 note.setContent(keyStoreService.encryptDataWithAES(content, decryptedAesKeyFront, ivFront));

                 noteEntityArrayList.add(note);
             }
        }
        return noteEntityArrayList;
    }

    @Override
    public NoteEntityUpdateDTO updateNote(String email, NoteEntityUpdateDTO noteEntityUpdateDTO) throws Exception {

        NoteEntityUpdateDTO responseNoteEntityUpdateDTO = new NoteEntityUpdateDTO();
        responseNoteEntityUpdateDTO.setId(noteEntityUpdateDTO.getId());
        responseNoteEntityUpdateDTO.setTitle(noteEntityUpdateDTO.getTitle());
        responseNoteEntityUpdateDTO.setContent(noteEntityUpdateDTO.getContent());

        byte[] aesKey = keyStoreService.decryptAES(noteEntityUpdateDTO.getAesKey());
        byte[] ivFront = keyStoreService.exportBase64ToArray(noteEntityUpdateDTO.getIvFront());
        byte[] encryptedMasterKey = keyStoreService.exportBase64ToArray(noteEntityUpdateDTO.getMasterKey());

        String masterKey = keyStoreService.decryptMasterKey(encryptedMasterKey);

        Optional<UserEntity> userExisting = userRepository.findByUsername(email);
        if (userExisting.isPresent()) {
            UserEntity user = userExisting.get();
            byte[] salt = user.getUserSalt();
            byte[] iv = user.getUserIv();

            SecretKey derivedKey = encryptionUtils.deriveKey(masterKey, salt);

            for(NoteEntity note: user.getListNotes()) {

                if(note.getId().equals(noteEntityUpdateDTO.getId())) {

                    if(noteEntityUpdateDTO.getTitle() != null) {
                        byte[] encryptedTitle = keyStoreService.exportBase64ToArray(noteEntityUpdateDTO.getTitle());
                        String decryptedTitle = keyStoreService.decryptDataWithAES(encryptedTitle, aesKey, ivFront);
                        String encryptTitle = encryptionUtils.encrypt(decryptedTitle, derivedKey, iv);
                        note.setTitle(encryptTitle);

                    } if(noteEntityUpdateDTO.getContent() != null) {
                        byte[] encryptedContent = keyStoreService.exportBase64ToArray(noteEntityUpdateDTO.getContent());
                        String decryptedContent = keyStoreService.decryptDataWithAES(encryptedContent, aesKey, ivFront);
                        String encryptContent = encryptionUtils.encrypt(decryptedContent, derivedKey, iv);
                        note.setContent(encryptContent);
                    }
                    userRepository.save(user);
                    break;
                }
            }
            return responseNoteEntityUpdateDTO;
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
