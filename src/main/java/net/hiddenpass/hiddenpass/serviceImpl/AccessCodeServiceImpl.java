package net.hiddenpass.hiddenpass.serviceImpl;

import me.gosimple.nbvcxz.resources.Generator;
import net.hiddenpass.hiddenpass.models.AccessCodeEntity;
import net.hiddenpass.hiddenpass.repository.AccessCodeRepository;
import net.hiddenpass.hiddenpass.service.AccessCodeService;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class AccessCodeServiceImpl implements AccessCodeService {

    private final AccessCodeRepository accessCodeRepository;

    public AccessCodeServiceImpl(AccessCodeRepository accessCodeRepository) {
        this.accessCodeRepository = accessCodeRepository;
    }

    /**
     * get the access code
     * @param id id is the code
     * @return object accessCodeEntity
     */
    @Override
    public Optional<AccessCodeEntity> getAccessCode(Long id) {
        return accessCodeRepository.findById(id);
    }

    /**
     * generate access code to give permission of ADMIN
     * @return object accessCodeEntity
     */
    @Override
    public AccessCodeEntity generateAccessCode() {
        String code = Generator.generateRandomPassword(Generator.CharacterTypes.NUMERIC, 6);
        AccessCodeEntity accessCodeEntity = new AccessCodeEntity();
        accessCodeEntity.setId(Long.parseLong(code));
        accessCodeEntity.setActive(true);
        accessCodeRepository.save(accessCodeEntity);
        return accessCodeEntity;
    }

    /**
     * delete access code
     * @param id id access code
     * @return object access code or empty
     */
    @Override
    public Optional<AccessCodeEntity> deleteAccessCode(Long id) {
        Optional<AccessCodeEntity> accessCode = accessCodeRepository.findById(id);

        if (accessCode.isPresent()) {
            accessCodeRepository.deleteById(id);
            return accessCode;
        }
        return Optional.empty();
    }
}
