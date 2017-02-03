package ru.ocelotjungle.bbyaplugin_gc.commands.manage;

public class IncorrectArgumentCountException extends IllegalArgumentException {
	private static final long serialVersionUID = 1349464501985209856L;

	public IncorrectArgumentCountException(String message) {
		super(message);
	}
}
