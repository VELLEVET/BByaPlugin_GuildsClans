package ru.OcelotJungle.BByaPlugin_GC.Commands.Manage;

public class IncorrectArgumentCountException extends IllegalArgumentException {
	private static final long serialVersionUID = 1349464501985209856L;

	public IncorrectArgumentCountException(String message) {
		super(message);
	}
}
