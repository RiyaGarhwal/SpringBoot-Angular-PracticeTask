package com.rays.ctl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rays.common.ORSResponse;
import com.rays.dto.TaskDTO;
import com.rays.form.TaskForm;
import com.rays.service.TaskService;

@RestController
@RequestMapping(value = "task")
public class TaskCtl {
	
	 @Autowired
	    public TaskService taskService;


	 
	 @PostMapping("save")
	 public ORSResponse save(@RequestBody @Valid TaskForm form, BindingResult bindingResult) {

	     System.out.println("Saving task: " + form);

	     ORSResponse res = validate(bindingResult);

	     if (!res.isSuccess()) {
	         return res;
	     }

	     TaskDTO dto = new TaskDTO();
	     dto.setId(form.getId());

	     // ✅ Manually set form values into DTO
	     dto.setName(form.getName());
	     dto.setTaskname(form.getTaskname());
	     dto.setDueDate(form.getDueDate());
	     dto.setStatus(form.getStatus());

	     if (dto.getId() != null && dto.getId() > 0) {
	         taskService.update(dto);
	         res.addData(dto.getId());
	         res.addMessage("Task updated successfully!");
	     } else {
	         long pk = taskService.add(dto);
	         res.addData(pk);
	         res.addMessage("Task added successfully!");
	     }

	     return res;
	 }
	 
//	 @PostMapping("save")
//	 public ORSResponse save(@RequestBody @Valid TaskForm form, BindingResult bindingResult) {
//
//	     System.out.println("Saving task: " + form);
//
//	     ORSResponse res = validate(bindingResult);
//
//	     if (!res.isSuccess()) {
//	         return res;
//	     }
//
//	     TaskDTO dto = new TaskDTO();
//
//	     // ✅ Set form values into DTO
//	     dto.setId(form.getId()); // 👈 Important for update
//	     dto.setName(form.getName());
//	     dto.setTaskname(form.getTaskname());
//	     dto.setDueDate(form.getDueDate());
//	     dto.setStatus(form.getStatus());
//
//	     if (dto.getId() != null && dto.getId() > 0) {
//	         taskService.update(dto);
//	         res.addData(dto.getId());
//	         res.addMessage("Task updated successfully!");
//	     } else {
//	         long pk = taskService.add(dto);
//	         res.addData(pk);
//	         res.addMessage("Task added successfully!");
//	     }
//
//	     return res;
//	 }



	    @GetMapping("delete/{ids}")
	    public ORSResponse delete(@PathVariable long[] ids) {

	        ORSResponse res = new ORSResponse();

	        for (long id : ids) {
	            taskService.delete(id);
	        }

	        res.addMessage("Task(s) deleted successfully");

	        return res;
	    }

	    @PostMapping("search/{pageNo}")
	    public ORSResponse search(@RequestBody TaskDTO form, @PathVariable int pageNo) {

	        ORSResponse res = new ORSResponse();
	        TaskDTO dto = form;
	        //TaskDTO dto = new TaskDTO();
	        // Optionally set search filters from form to dto

	        List list = taskService.search(dto, pageNo, 5);

	        if (list.size() == 0) {
	            res.addMessage("No task found!");
	        } else {
	            res.addData(list);
	        }

	        return res;
	    }

	    public ORSResponse validate(BindingResult bindingResult) {

	        ORSResponse res = new ORSResponse(true);

	        if (bindingResult.hasErrors()) {

	            res.setSuccess(false);

	            Map<String, String> errors = new HashMap<>();

	            List<FieldError> list = bindingResult.getFieldErrors();

	            list.forEach(e -> {
	                errors.put(e.getField(), e.getDefaultMessage());
	            });

	            res.addInputError(errors);
	        }

	        return res;
	 
	
	
	    }
	    
	    @GetMapping("get/{id}")
		public ORSResponse get(@PathVariable long id) {							

			System.out.println("BaseCtl Get() method run.......Rahul");

			ORSResponse res = new ORSResponse(true);
			
			TaskDTO dto = taskService.findById(id );

			if (dto != null) {
				res.addData(dto);
			} else {
				

				res.setSuccess(false);
				
				res.addMessage("Record not found");
			}
			System.out.println("Edit response :" + res);
			return res;
		}
	    
//	    @PutMapping("update")
//	    public ORSResponse update(@RequestBody @Valid TaskForm form, BindingResult bindingResult) {
//
//	        System.out.println("Updating Task: " + form);
//
//	        ORSResponse res = validate(bindingResult);
//
//	        if (!res.isSuccess()) {
//	            return res;
//	        }
//
//	        TaskDTO dto = new TaskDTO();
//
//	        // 🟢 Mapping form to dto
//	        dto.setId(form.getId());
//	        dto.setName(form.getName());
//	        dto.setTaskname(form.getTaskname());
//	        dto.setDueDate(form.getDueDate());
//	        dto.setStatus(form.getStatus());
//
//	        // 🛠 Update call to service
//	        taskService.update(dto);
//	        res.addData(dto.getId());
//	        res.addMessage("Task updated successfully!");
//
//	        return res;
//	    }


		
}
