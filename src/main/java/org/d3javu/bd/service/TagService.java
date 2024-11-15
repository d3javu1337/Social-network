package org.d3javu.bd.service;

import lombok.RequiredArgsConstructor;
import org.d3javu.bd.dto.tag.TagDto;
import org.d3javu.bd.mapper.tag.DtoToTagMapper;
import org.d3javu.bd.mapper.tag.TagToDtoMapper;
import org.d3javu.bd.models.post.Post;
import org.d3javu.bd.models.tag.Tag;
import org.d3javu.bd.repositories.TagRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class TagService {

    private final TagRepository tagRepository;
    private final PostService postService;
    private final TagToDtoMapper tagToDtoMapper;
    private final DtoToTagMapper dtoToTagMapper;

    @Transactional
    public TagDto create(TagDto tagDto) {
        Tag tag = this.dtoToTagMapper.map(tagDto);
        this.tagRepository.saveAndFlush(tag);
        return tagToDtoMapper.map(tag);
    }

    public Set<TagDto> findAll() {
        Set<TagDto> tags = new HashSet<>(this.tagRepository.findAll()
                .stream().map(this.tagToDtoMapper::map).collect(Collectors.toSet()));
        return tags;
    }

    public Optional<TagDto> findById(Long id) {
        return this.tagRepository.findById(id).map(this.tagToDtoMapper::map);
    }

    @Transactional
    public Optional<TagDto> update(TagDto tagDto) {
        return this.tagRepository.findByBody(tagDto.getBody())
                .map(t -> this.dtoToTagMapper.map(tagDto, t))
                .map(this.tagRepository::saveAndFlush)
                .map(this.tagToDtoMapper::map);
    }

    @Transactional
    public Optional<TagDto> update(Long id, TagDto tagDto) {
        return this.tagRepository.findById(id)
                .map(t -> this.dtoToTagMapper.map(tagDto, t))
                .map(this.tagRepository::saveAndFlush)
                .map(this.tagToDtoMapper::map);
    }

    @Transactional
    public boolean delete(Long id){
        return this.tagRepository.findById(id)
                .map(tag -> {
                    this.tagRepository.delete(tag);
                    this.tagRepository.flush();
                    return true;
                })
                .orElse(false);
    }

    public Set<TagDto> findByPost(Post post) {
        return this.tagRepository.findTagsIdByPostId(post.getId()).stream().map(tagToDtoMapper::map).collect(Collectors.toSet());
    }

}
