package net.hiddenpass.hiddenpass.service;

import net.hiddenpass.hiddenpass.models.AccessCodeEntity;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface AccessCodeService {
    Optional<AccessCodeEntity> getAccessCode(Long id);
    AccessCodeEntity generateAccessCode();
    Optional<AccessCodeEntity> deleteAccessCode(Long accessCodeId);
}
