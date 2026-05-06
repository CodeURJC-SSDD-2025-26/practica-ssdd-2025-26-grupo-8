package es.urjc.virtusfitness.mapper;

import es.urjc.virtusfitness.dto.FitnessClassCreateDto;
import es.urjc.virtusfitness.dto.FitnessClassDto;
import es.urjc.virtusfitness.dto.FitnessClassUpdateDto;
import es.urjc.virtusfitness.model.FitnessClass;

public final class FitnessClassMapper {

  private FitnessClassMapper() {}

  public static FitnessClassDto toDto(FitnessClass fc) {
    if (fc == null) return null;
    return new FitnessClassDto(
        fc.getId(),
        fc.getName(),
        fc.getDescription(),
        fc.getInstructor(),
        fc.getDuration(),
        fc.getCapacity(),
        fc.getDifficulty(),
        fc.getCategory(),
        fc.getSchedule(),
        fc.getPrice(),
        fc.isActive(),
        fc.getImage() != null,
        fc.getAvailableSpots(),
        fc.getAverageRating());
  }

  public static FitnessClass toEntity(FitnessClassCreateDto dto) {
    FitnessClass fc = new FitnessClass();
    fc.setName(dto.name());
    fc.setDescription(dto.description());
    fc.setInstructor(dto.instructor());
    fc.setDuration(dto.duration());
    fc.setCapacity(dto.capacity());
    fc.setDifficulty(dto.difficulty());
    fc.setCategory(dto.category());
    fc.setSchedule(dto.schedule());
    fc.setPrice(dto.price());
    fc.setActive(dto.active());
    return fc;
  }

  public static void applyUpdate(FitnessClassUpdateDto dto, FitnessClass target) {
    if (dto.name() != null) target.setName(dto.name());
    if (dto.description() != null) target.setDescription(dto.description());
    if (dto.instructor() != null) target.setInstructor(dto.instructor());
    if (dto.duration() != null) target.setDuration(dto.duration());
    if (dto.capacity() != null) target.setCapacity(dto.capacity());
    if (dto.difficulty() != null) target.setDifficulty(dto.difficulty());
    if (dto.category() != null) target.setCategory(dto.category());
    if (dto.schedule() != null) target.setSchedule(dto.schedule());
    if (dto.price() != null) target.setPrice(dto.price());
    if (dto.active() != null) target.setActive(dto.active());
  }
}
