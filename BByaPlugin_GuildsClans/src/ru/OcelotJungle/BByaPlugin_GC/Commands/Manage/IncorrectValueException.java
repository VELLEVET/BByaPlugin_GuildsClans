package ru.ocelotjungle.bbyaplugin_gc.commands.manage;

public class IncorrectValueException extends IllegalArgumentException {
	private static final long serialVersionUID = 8112127856323825046L;

	public IncorrectValueException(String message) {
		super(message);
	}
}
