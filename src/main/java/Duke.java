import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Scanner;

public class Duke {

    private ArrayList<Task> tasks;

    public Duke() {
        this.tasks = new ArrayList<Task>(100);
    }

    private void start() {
        System.out.println("Hello! I'm Duke\nWhat can I do for you?");

        Scanner sc = new Scanner(System.in);
        String userInput = sc.nextLine();

        // check userInput
        while (!userInput.equals("bye")) {
            String[] inputStringArray = userInput.split(" ", 2);
            try {
                switch (inputStringArray[0]) {
                    case "list":
                        this.printTasks();
                        break;
                    case "done":
                        if (inputStringArray.length < 2) {
                            throw new DukeException("Please specify a task number.");
                        }
                        try {
                            int taskIndex = Integer.parseInt(inputStringArray[1]) - 1;
                            Task doneTask = tasks.get(taskIndex);
                            doneTask.setDone();
                            System.out.println("Nice! I've marked this task as done:\n  " + doneTask.toString());
                            break;
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            throw new DukeException("Please specify a valid task number.");
                        }
                    case "delete":
                        if (inputStringArray.length < 2) {
                            throw new DukeException("Please specify a task number.");
                        }
                        try {
                            int taskIndex = Integer.parseInt(inputStringArray[1]) - 1;
                            Task deletedTask = tasks.get(taskIndex);
                            tasks.remove(taskIndex);
                            System.out.println("Noted. I've removed this task:\n  " + deletedTask.toString());
                            System.out.println("Now you have " + tasks.size() + " tasks in the list.");
                            break;
                        } catch (NumberFormatException | IndexOutOfBoundsException e) {
                            throw new DukeException("Please specify a valid task number.");
                        }
                    case "deadline":
                        if (inputStringArray.length < 2) {
                            throw new DukeException("Please specify the task info.");
                        }
                        String[] deadlineInfo = inputStringArray[1].split(" /by ", 2);
                        if (deadlineInfo.length < 2) {
                            throw new DukeException("Please enter a valid deadline format.");
                        }
                        try {
                            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("ddMMyy");
                            LocalDate by = LocalDate.parse(deadlineInfo[1], dateFormatter);
                            Task newDeadline = new Deadline(deadlineInfo[0], by);
                            tasks.add(newDeadline);
                            System.out.println("Got it. I've added this task:\n  " + newDeadline.toString());
                            break;
                        } catch (DateTimeParseException e) {
                            throw new DukeException("Please enter a valid date.");
                        }
                    case "event":
                        if (inputStringArray.length < 2) {
                            throw new DukeException("Please specify the task info.");
                        }
                        String[] eventInfo = inputStringArray[1].split(" /at ", 2);
                        if (eventInfo.length < 2) {
                            throw new DukeException("Please enter a valid event format.");
                        }
                        try {
                            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("ddMMyy HHmm");
                            LocalDateTime at = LocalDateTime.parse(eventInfo[1], dateTimeFormatter);
                            Task newEvent = new Event(eventInfo[0], at);
                            tasks.add(newEvent);
                            System.out.println("Got it. I've added this task:\n" + "  " + newEvent.toString());
                            break;
                        } catch (DateTimeParseException e) {
                            throw new DukeException("Please enter a valid date.");
                        }
                    case "todo":
                        if (inputStringArray.length < 2) {
                            throw new DukeException("Please specify the task info.");
                        }
                        Task newToDo = new ToDo(inputStringArray[1]);
                        tasks.add(newToDo);
                        System.out.println("Got it. I've added this task:\n" + "  " + newToDo.toString());
                        break;
                    default:
                        throw new DukeException("Sorry, I don't know what that means.");
                }
            } catch (DukeException e) {
                System.out.println(e.getMessage());
            }
            userInput = sc.nextLine(); // get new userInput
        }

        System.out.println("Bye. Hope to see you again soon!");
    }

    private void printTasks() {
        System.out.println("Here are the tasks in your list:");
        int taskCount = tasks.size();
        for (int i = 0; i < taskCount; i++) {
            System.out.println((i + 1) + ". " + tasks.get(i).toString());
        }
    }

    public static void main(String[] args) {
        Duke duke = new Duke();
        duke.start();
    }

}
