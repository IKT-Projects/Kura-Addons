/*
 * Copyright © 2023 Institut fuer Kommunikationstechnik - FH-Dortmund (codebase.ikt@fh-dortmund.de)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.ict.kura.thing.model;

public class FormConfig {
	private String href;
	private String contentType;
	private String subprotocol;

	public FormConfig() {
		this.contentType = "application/json";
		this.subprotocol = "https";
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getSubprotocol() {
		return subprotocol;
	}

	public void setSubprotocol(String subprotocol) {
		this.subprotocol = subprotocol;
	}

	@Override
	public String toString() {
		return "FormConfig [href=" + href + ", contentType=" + contentType + ", subprotocol=" + subprotocol + "]";
	}
}
