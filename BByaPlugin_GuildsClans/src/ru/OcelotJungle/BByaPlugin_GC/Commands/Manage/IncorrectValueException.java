package ru.OcelotJungle.BByaPlugin_GC.Commands.Manage;

public class IncorrectValueException extends IllegalArgumentException {
	private static final long serialVersionUID = 8112127856323825046L;

	public IncorrectValueException(String message) {
		super(message);
	}
}
