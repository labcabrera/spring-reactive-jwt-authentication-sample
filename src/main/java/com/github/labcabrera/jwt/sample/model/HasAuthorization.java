package com.github.labcabrera.jwt.sample.model;

import java.util.List;

public interface HasAuthorization extends HasId {

	List<String> getAuthorization();

	void setAuthorization(List<String> permissions);

}
