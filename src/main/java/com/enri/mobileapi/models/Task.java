package com.enri.mobileapi.models;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.fasterxml.jackson.annotation.JsonInclude;

@Entity
@Table(name = "tasks")
public class Task implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String name;
	
	private Long startAt;
	
	private Long endAt;
	
	private String status;
	
	private String assignedContent;
	
	private String handlingContent;
	
	private String comment;
	
	private Double rating;
	
	private Long evaluatedAt;
	
	private Long committedAt;
	
	@Lob
	private String image;
	
	private Long approvedAt;
	
	private Long oldTask;
	
	private Long assignerId;
	
	private Long assigneeId;
	
	@JsonInclude()
	@Transient
	private String assignee;
	
	@JsonInclude()
	@Transient
	private String assigner;
	
	@JsonInclude()
	@Transient
	private String approver;
	
	@JsonInclude()
	@Transient
	private String commenter;
	
	@JsonInclude()
	@Transient
	private String oldTaskName;
	
	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getCommenter() {
		return commenter;
	}

	public void setCommenter(String commenter) {
		this.commenter = commenter;
	}

	private Long approverId;
	
	private Long commenterId;
	
	private Boolean isDeleted;
	
	private Long creatorId;
	
	private Long updaterId;
	
	private Long createdAt;
	
	private Long updatedAt;

	public Task() {
		super();
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Long getStartAt() {
		return startAt;
	}

	public void setStartAt(Long startAt) {
		this.startAt = startAt;
	}

	public Long getEndAt() {
		return endAt;
	}

	public void setEndAt(Long endAt) {
		this.endAt = endAt;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getAssignedContent() {
		return assignedContent;
	}

	public void setAssignedContent(String assignedContent) {
		this.assignedContent = assignedContent;
	}

	public String getHandlingContent() {
		return handlingContent;
	}

	public void setHandlingContent(String handlingContent) {
		this.handlingContent = handlingContent;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public Double getRating() {
		return rating;
	}

	public void setRating(Double rating) {
		this.rating = rating;
	}

	public Long getEvaluatedAt() {
		return evaluatedAt;
	}

	public void setEvaluatedAt(Long evaluatedAt) {
		this.evaluatedAt = evaluatedAt;
	}

	public Long getCommittedAt() {
		return committedAt;
	}

	public void setCommittedAt(Long committedAt) {
		this.committedAt = committedAt;
	}

	public Long getApprovedAt() {
		return approvedAt;
	}

	public void setApprovedAt(Long approvedAt) {
		this.approvedAt = approvedAt;
	}

	public Long getOldTask() {
		return oldTask;
	}

	public void setOldTask(Long oldTask) {
		this.oldTask = oldTask;
	}

	public Long getAssignerId() {
		return assignerId;
	}

	public void setAssignerId(Long assignerId) {
		this.assignerId = assignerId;
	}

	public Long getAssigneeId() {
		return assigneeId;
	}

	public void setAssigneeId(Long assigneeId) {
		this.assigneeId = assigneeId;
	}

	public Long getApproverId() {
		return approverId;
	}

	public void setApproverId(Long approverId) {
		this.approverId = approverId;
	}

	public Long getCommenterId() {
		return commenterId;
	}

	public void setCommenterId(Long commenterId) {
		this.commenterId = commenterId;
	}

	public Boolean getIsDeleted() {
		return isDeleted;
	}

	public void setIsDeleted(Boolean isDeleted) {
		this.isDeleted = isDeleted;
	}

	public Long getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(Long creatorId) {
		this.creatorId = creatorId;
	}

	public Long getUpdaterId() {
		return updaterId;
	}

	public void setUpdaterId(Long updaterId) {
		this.updaterId = updaterId;
	}

	public Long getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Long createdAt) {
		this.createdAt = createdAt;
	}

	public Long getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Long updatedAt) {
		this.updatedAt = updatedAt;
	}

	public String getImage() {
		return image;
	}

	public void setImage(String image) {
		this.image = image;
	}

	public String getAssignee() {
		return assignee;
	}

	public void setAssignee(String assignee) {
		this.assignee = assignee;
	}

	public String getAssigner() {
		return assigner;
	}

	public void setAssigner(String assigner) {
		this.assigner = assigner;
	}

	public String getOldTaskName() {
		return oldTaskName;
	}

	public void setOldTaskName(String oldTaskName) {
		this.oldTaskName = oldTaskName;
	}
}
