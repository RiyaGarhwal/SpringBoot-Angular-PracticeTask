package com.rays.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.rays.dao.TaskDAO;
import com.rays.dto.TaskDTO;


@Service
@Transactional
public class TaskService {
	@Autowired
		public TaskDAO dao;
		
		public long add(TaskDTO dto) {
			long pk = dao.add(dto);
			return pk;
		}
	 
		@Transactional(propagation = Propagation.REQUIRED)
		public void update(TaskDTO dto) {
			dao.update(dto);
		}
	 
		@Transactional(propagation = Propagation.REQUIRED)
		public void delete(long id) {
			try {
				TaskDTO dto = findById(id);
				dao.delete(dto);
			} catch (RuntimeException e) {
				System.out.println(e.getMessage());
			}
		}
		
		@Transactional(readOnly = true)
		public TaskDTO findById(long id) {
			TaskDTO dto = dao.findByPK(id);
			return dto;
		}
	 
		@Transactional(propagation = Propagation.REQUIRED)
		public long save(TaskDTO dto) {
			Long id = dto.getId();
			if (id != null && id > 0) {
				update(dto);
			} else {
				id = add(dto);
			}
			return id;
		}
	 
		@Transactional(readOnly = true)
		public List search(TaskDTO dto, int pageNo, int pageSize) {
			List list = dao.search(dto, pageNo, pageSize);
			return list;
		}
	 
//		@Transactional(readOnly = true)
//		public TaskDTO findByLogin(String login) {
//			TaskDTO dto = dao.findByUniqueKey("loginId", login);
//			return dto;
//		}
//		

}
