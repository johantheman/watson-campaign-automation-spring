package com.github.ka4ok85.wca.response;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component
@Scope("prototype")
public class UpdateRecipientResponse extends AbstractResponse {
	private Long recipientId;
	private boolean visitorAssociation;

	public Long getRecipientId() {
		return recipientId;
	}

	public void setRecipientId(Long recipientId) {
		this.recipientId = recipientId;
	}

	public boolean isVisitorAssociation() {
		return visitorAssociation;
	}

	public void setVisitorAssociation(boolean visitorAssociation) {
		this.visitorAssociation = visitorAssociation;
	}

	@Override
	public String toString() {
		return "UpdateRecipientResponse [recipientId=" + recipientId + ", visitorAssociation=" + visitorAssociation
				+ "]";
	}

}
