package seedu.siasa.logic.parser.client;

import static seedu.siasa.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_ADDRESS;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_EMAIL;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_PHONE;
import static seedu.siasa.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;
import java.util.stream.Stream;

import seedu.siasa.logic.commands.client.AddClientCommand;
import seedu.siasa.logic.parser.exceptions.ParseException;
import seedu.siasa.model.person.Address;
import seedu.siasa.model.person.Email;
import seedu.siasa.model.person.Name;
import seedu.siasa.model.person.Person;
import seedu.siasa.model.person.Phone;
import seedu.siasa.model.tag.Tag;
import seedu.siasa.logic.parser.ArgumentMultimap;
import seedu.siasa.logic.parser.ArgumentTokenizer;
import seedu.siasa.logic.parser.Parser;
import seedu.siasa.logic.parser.ParserUtil;
import seedu.siasa.logic.parser.Prefix;

/**
 * Parses input arguments and creates a new AddCommand object
 */
public class AddClientCommandParser implements Parser<AddClientCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the AddCLientCommand
     * and returns an AddClientCommand object for execution.
     * @throws ParseException if the user input does not conform the expected format
     */
    public AddClientCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_NAME, PREFIX_PHONE, PREFIX_EMAIL, PREFIX_ADDRESS, PREFIX_TAG);

        if (!arePrefixesPresent(argMultimap, PREFIX_NAME, PREFIX_ADDRESS, PREFIX_PHONE, PREFIX_EMAIL)
                || !argMultimap.getPreamble().isEmpty()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddClientCommand.MESSAGE_USAGE));
        }

        Name name = ParserUtil.parseName(argMultimap.getValue(PREFIX_NAME).get());
        Phone phone = ParserUtil.parsePhone(argMultimap.getValue(PREFIX_PHONE).get());
        Email email = ParserUtil.parseEmail(argMultimap.getValue(PREFIX_EMAIL).get());
        Address address = ParserUtil.parseAddress(argMultimap.getValue(PREFIX_ADDRESS).get());
        Set<Tag> tagList = ParserUtil.parseTags(argMultimap.getAllValues(PREFIX_TAG));

        Person person = new Person(name, phone, email, address, tagList);

        return new AddClientCommand(person);
    }

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

}
