package com.ythwork.minisoda.account.hateoas;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import com.ythwork.minisoda.account.dto.AccountInfo;
import com.ythwork.minisoda.account.web.AccountController;

@Component
public class AccountModelAssembler implements RepresentationModelAssembler<AccountInfo, EntityModel<AccountInfo>> {

	@Override
	public EntityModel<AccountInfo> toModel(AccountInfo accountInfo) {
		return EntityModel.of(accountInfo, 
				linkTo(methodOn(AccountController.class).getAccount(accountInfo.getAccountId())).withSelfRel(),
				linkTo(methodOn(AccountController.class).allAccounts(null)).withRel("accounts"));
	}

}
