package com.ythwork.minisoda.login.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ythwork.minisoda.login.dto.LoginResponse;
import com.ythwork.minisoda.member.web.MemberController;

@Component
public class LoginModelAssembler implements RepresentationModelAssembler<LoginResponse, EntityModel<LoginResponse>> {

	@Override
	public EntityModel<LoginResponse> toModel(LoginResponse loginResponse) {
		return EntityModel.of(loginResponse, 
				linkTo(methodOn(MemberController.class).getMember(loginResponse.getMemberInfo().getMemberId(), null)).withRel("member"));
	}

}
