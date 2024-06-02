package hexlet.code.app.service;

import hexlet.code.app.dto.label.LabelCreateDTO;
import hexlet.code.app.dto.label.LabelDTO;
import hexlet.code.app.dto.label.LabelUpdateDTO;
import hexlet.code.app.mapper.LabelMapper;
import hexlet.code.app.repository.LabelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelService {


    @Autowired
    private LabelMapper labelMapper;

    @Autowired
    private LabelRepository labelRepository;


    public Long countAll() {
        return labelRepository.count();
    }

    public List<LabelDTO> getAll() {

        return labelRepository.findAll()
                .stream()
                .map(labelMapper::map)
                .toList();
    }


    public LabelDTO findById(Long id) {
        var label = labelRepository.findById(id)
                .orElseThrow();

        return labelMapper.map(label);
    }

    public LabelDTO create(LabelCreateDTO data) {
        var label = labelMapper.map(data);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public LabelDTO update(Long id, LabelUpdateDTO data) {
        var label = labelRepository.findById(id)
                .orElseThrow();
        labelMapper.update(data, label);
        labelRepository.save(label);
        return labelMapper.map(label);
    }

    public void delete(Long id) {
        labelRepository.deleteById(id);
    }
}
