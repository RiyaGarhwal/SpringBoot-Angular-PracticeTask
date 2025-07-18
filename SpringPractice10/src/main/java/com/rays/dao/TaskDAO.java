package com.rays.dao;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;

import org.springframework.stereotype.Repository;

import com.rays.dto.TaskDTO;


@Repository
public class TaskDAO {

    @PersistenceContext
    private EntityManager entityManager;

    // ✅ Add Task
    public Long add(TaskDTO dto ) {
        

        entityManager.persist(dto);
        return dto.getId();
    }

    // ✅ Update Task
    public void update(TaskDTO dto) {
       
        entityManager.merge(dto);
    }

    // ✅ Delete Task
    public void delete(TaskDTO dto) {
        entityManager.remove(dto);
    }

    // ✅ Delete by ID
    public void deleteById(Long id) {
        TaskDTO dto = findByPK(id);
        if (dto != null) {
            delete(dto);
        }
    }

    // ✅ Find by Primary Key
    public TaskDTO findByPK(Long id) {
        return entityManager.find(TaskDTO.class, id);
    }

    public List<TaskDTO> search(TaskDTO dto, int pageNo, int pageSize) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<TaskDTO> cq = builder.createQuery(TaskDTO.class);
        Root<TaskDTO> root = cq.from(TaskDTO.class);

        Predicate predicate = builder.conjunction(); // Start with "true"

        if (dto.getName() != null && !dto.getName().isEmpty()) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("name")), "%" + dto.getName().toLowerCase() + "%"));
        }

        if (dto.getTaskname() != null && !dto.getTaskname().isEmpty()) {
            predicate = builder.and(predicate, builder.like(builder.lower(root.get("taskname")), "%" + dto.getTaskname().toLowerCase() + "%"));
        }

        if (dto.getStatus() != null && !dto.getStatus().isEmpty()) {
            predicate = builder.and(predicate, builder.equal(builder.lower(root.get("status")), dto.getStatus().toLowerCase()));
        }

        if (dto.getDueDate() != null) {
            predicate = builder.and(predicate, builder.equal(root.get("dueDate"), dto.getDueDate()));
        }

        cq.where(predicate);

        TypedQuery<TaskDTO> query = entityManager.createQuery(cq);
        if (pageSize > 0) {
            query.setFirstResult(pageNo * pageSize);
            query.setMaxResults(pageSize);
        }

        return query.getResultList();
    }

    // ✅ Overloaded search without pagination
    public List<TaskDTO> search(TaskDTO dto) {
        return search(dto, 0, 0);
    }

   
    }

   
