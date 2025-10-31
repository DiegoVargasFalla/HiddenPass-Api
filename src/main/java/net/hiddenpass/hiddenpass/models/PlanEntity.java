package net.hiddenpass.hiddenpass.models;

import jakarta.persistence.*;
import net.hiddenpass.hiddenpass.enumerations.EBenefits;
import net.hiddenpass.hiddenpass.enumerations.ENamePlan;
import net.hiddenpass.hiddenpass.enumerations.ETypeRenovation;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "plans")
public class PlanEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private ENamePlan name;
    private String description;
    private float price;

    @ElementCollection(fetch = FetchType.LAZY, targetClass = EBenefits.class)
    @CollectionTable(name = "plans_benefits", joinColumns = @JoinColumn(name = "plan_id"))
    @Enumerated(EnumType.STRING)
    private Set<EBenefits> benefitsSet;

    @Enumerated(EnumType.STRING)
    private ETypeRenovation typeRenovation;

    private LocalDate creationDate;

    public PlanEntity() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public ENamePlan getName() {
        return name;
    }

    public void setName(ENamePlan name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public float getPrice() {
        return price;
    }

    public void setPrice(float price) {
        this.price = price;
    }

    public Set<EBenefits> getBenefitsSet() {
        return benefitsSet;
    }

    public void setBenefitsSet(Set<EBenefits> benefitsSet) {
        this.benefitsSet = benefitsSet;
    }

    public ETypeRenovation getTypeRenovation() {
        return typeRenovation;
    }

    public void setTypeRenovation(ETypeRenovation typeRenovation) {
        this.typeRenovation = typeRenovation;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
