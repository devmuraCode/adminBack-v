package uz.project.entity.translation;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import uz.project.common.constant.TranslationType;
import uz.project.common.exception.ApiException;
import uz.project.common.request.PageableRequest;
import uz.project.common.request.PageableRequestUtil;
import uz.project.common.request.SearchSpecification;
import uz.project.common.response.status.TranslationStatus;

import java.util.List;
import java.util.Optional;

@Service
public class TranslationService {

    private final TranslationRepository translationRepository;
    private final JdbcTemplate jdbcTemplate;

    public TranslationService(TranslationRepository translationRepository, JdbcTemplate jdbcTemplate) {
        this.translationRepository = translationRepository;
        this.jdbcTemplate = jdbcTemplate;
    }

    public Optional<Translation> findById(Long id) {
        if (id == null) return Optional.empty();
        return translationRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Page<Translation> getList(PageableRequest pageableRequest, Pageable pageable) {
        return translationRepository.findAll(new SearchSpecification<>(pageableRequest.getSearch()), pageable);
    }

    @Transactional(readOnly = true)
    public Page<Translation> getList(PageableRequest pageable) {
        return translationRepository.findAll(new SearchSpecification<>(pageable.getSearch()), PageableRequestUtil.toPageable(pageable));
    }

    public List<TranslationMin> getAllByType(TranslationType type) {
        String sql = """
                SELECT t.id as id,
                       t.name ->> 'uz' as name_uz,
                       t.name ->> 'oz' as name_oz,
                       t.name ->> 'ru' as name_ru,
                       t.tag as tag_name
                FROM translations t WHERE (t.types)::jsonb ? '#TYPE' AND t.status='ACTIVE'
                """;
        sql = sql.replace("#TYPE", type.name());
        return jdbcTemplate.query(sql, (rs, rowNum) -> new TranslationMin(rs));
    }

    @Transactional(readOnly = true)
    public Translation getById(Long id) {
        return findById(id).orElseThrow(() -> new ApiException(TranslationStatus.NOT_FOUND));
    }

    @Transactional
    public Translation save(Translation technology, TranslationPayload payload) {
        technology.setName(payload.getName());
        technology.setTag(payload.getTag());
        technology.setTypes(payload.getTypes());
        technology.setStatus(payload.getStatus());
        return translationRepository.saveAndFlush(technology);
    }

    public Translation create(TranslationPayload payload) {
        if (translationRepository.existsByTag(payload.getTag()))
            throw new ApiException(TranslationStatus.TAG_ALREADY_EXISTS);
        Translation technology = new Translation();
        return save(technology, payload);
    }

    public Translation update(Long id, TranslationPayload payload) {
        if (translationRepository.existsByIdNotAndTag(id, payload.getTag()))
            throw new ApiException(TranslationStatus.TAG_ALREADY_EXISTS);
        Translation technology = getById(id);
        return save(technology, payload);
    }

    public void delete(Long id) {
        Translation technology = getById(id);
        translationRepository.delete(technology);
    }
}
