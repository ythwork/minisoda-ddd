package com.ythwork.minisoda.member.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ythwork.minisoda.member.dto.MemberInfo;
import com.ythwork.minisoda.member.web.MemberController;

@Component
public class MemberModelAssembler implements RepresentationModelAssembler<MemberInfo, EntityModel<MemberInfo>> {
	@Override
	public EntityModel<MemberInfo> toModel(MemberInfo memberInfo) {
		return EntityModel.of(memberInfo, 
					linkTo(methodOn(MemberController.class).getMember(memberInfo.getMemberId(), null)).withSelfRel());
	}
}
