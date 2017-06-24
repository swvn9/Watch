package net.swvn9;

import net.dv8tion.jda.core.OnlineStatus;
import net.dv8tion.jda.core.entities.*;
import net.dv8tion.jda.core.events.*;
import net.dv8tion.jda.core.events.message.MessageReceivedEvent;
import net.dv8tion.jda.core.hooks.ListenerAdapter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

class ReadyListener implements net.dv8tion.jda.core.hooks.EventListener {
	@Override
	public void onEvent(net.dv8tion.jda.core.events.Event event)
	{
		if(event instanceof ReadyEvent){
			for(Guild a : event.getJDA().getGuilds()){
				switch (a.getId()) {
					default:
						EventListener.logger(EventListener.logPrefix(0) + "I'm in " + a.getName() + "! ID:" + a.getId());
						break;
					case "123527831664852992":
						event.getJDA().getPresence().setStatus(OnlineStatus.INVISIBLE);
						EventListener.logger(EventListener.logPrefix(0) + "I'm in seb's server!");
						break;
					case "243112682142695446":
						event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
						EventListener.logger(EventListener.logPrefix(0) + "I'm in the test server!");
						break;
					case "254861442799370240":
						event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
						EventListener.logger(EventListener.logPrefix(0) + "I'm in the Zamorak Cult public server");
						break;
					case "319606739550863360":
						event.getJDA().getPresence().setStatus(OnlineStatus.ONLINE);
						EventListener.logger(EventListener.logPrefix(0) + "I'm in the Zamorak Cult Administer Server");
						break;
				}
			}
			event.getJDA().getPresence().setGame(Game.of("::help | 1.9a"));
		}
	}
}

class EventListener extends ListenerAdapter {

	private static final String LITERAL = "::";
	static String WHITELIST[] = Config.getWhitelist();
	private static final String LOGTIME = new SimpleDateFormat("MMMDDYY_HHmmss").format(new Date());
    private static FileWriter log;
    private static Guild Home;

	//check if a specific channel ID is on the whitelist
    private boolean channelWhitelisted (String channelID){
        for(String value : WHITELIST){ //for each value in the whitelist array
            if(value.equalsIgnoreCase(channelID)) return true; //if the id is whitelisted, return true.
        }
    return false; //if it isn't in the whitelist, return false
    }

	//create a prefix for any log entries
	static String logPrefix(int type){
		String timeStamp = new SimpleDateFormat("HH:mm:ss").format(new Date()); //ge tthe current timestamp
		String logType; //prepare the log type string
		switch(type) {
			default:
				logType = "Info"; //Mark the type as Info
				break;
			case 1:
				logType = "Error"; //mark the type as Error
				break;
			case 2:
				logType = "Chat"; //mark the type as Chat
				break;
			case 3:
				logType = "Moji"; //mark the type as Emoji/Reaction
				break;
			case 4:
				logType = "CACH";
				break;
			case 5:
				logType = "DBUG";
				break;
			case 6:
				logType = "Perm";
				break;
		}
		return "["+timeStamp+"] ["+logType+"] [Log]: "; //create and return the log prefix
	}

	@SuppressWarnings("unused")
	static void logger(String input){
		try {
			EventListener.log =new FileWriter("Logs"+File.separator+"LOG_"+LOGTIME+".txt",true);
			log.write(input+System.lineSeparator());
			log.close();
			System.out.println(input);
		}catch(IOException e){
			e.printStackTrace();
		}
	}

	private static boolean isCommand(Message m){
		for(int i = 0; i< LITERAL.length(); i++){
			if(m.getContent().indexOf(LITERAL)!=0){
				return false;
			}
		}
		return true;
	}

    @Override //any message sent that the bot can see
    public void onMessageReceived(MessageReceivedEvent e) {
		if(BotCommands.c.getWaiting()&&e.getChannelType().equals(ChannelType.PRIVATE)&&e.getAuthor().getId().equals("320611242366730241")){
			BotCommands.c.lastchannel.sendMessage(e.getMessage().getRawContent()).queue(msg->msg.delete().queueAfter(30, TimeUnit.SECONDS));
			BotCommands.c.setWaiting(false);
		}
	    if((e.getAuthor().isBot()||
		        e.getAuthor().getAsMention().equals(Bot.jda.getSelfUser().getAsMention())||
		        (!channelWhitelisted(e.getChannel().getId()+"")&&!e.getMessage().getContent().contains("::pullconfig")&&!e.getMessage().getContent().contains("::clean")&&e.getMessage().getContent().contains("::help")||e.getChannelType().equals(ChannelType.PRIVATE)))){
            return;
        }
		if(Home==null) EventListener.Home= e.getGuild();

		String input = e.getMessage().getRawContent();
        if(e.isFromType(ChannelType.TEXT)) logger(logPrefix(2)+"("+e.getGuild().getName()+", #"+e.getTextChannel().getName()+") "+e.getAuthor().getName()+": "+input);
	    if(e.isFromType(ChannelType.PRIVATE)) logger(logPrefix(2)+"(Private Message) "+e.getAuthor().getName()+": "+input);

	    if(e.getChannel().getId().equals("320615332840472576")&&!e.getAuthor().isBot()){
            if(input.charAt(0)=='<'){
				BotCommands.verify.run(e.getMessage());
            }
        }

		if(e.getGuild().getId().equals("319606739550863360")){
	    	Scanner message = new Scanner(input);
	    	if(input.contains("vote")||input.contains("poll")||input.contains("Vote")||input.contains("Poll")){
				e.getChannel().sendMessage("<@&319607280540712961>, "+e.getMember().getEffectiveName()+" has called a vote! Leave your vote in the form of a reaction on this message!\n\n"+input).queue(msg->{
					while(message.hasNext()){
						String z = message.next();
						switch(z){
							default:
								break;
							case "\uD83C\uDDE6":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDE7":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDE8":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDE9":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDEA":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDEB":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDEC":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
							case "\uD83C\uDDED":
								e.getChannel().addReactionById(msg.getId(),z).queue();
								break;
						}
					}
				});
				if (e.getChannelType().isGuild()) e.getMessage().delete().queue();
			}
		}


		if((isCommand(e.getMessage()))){
			input=input.replaceFirst(LITERAL,"");
			Scanner command = new Scanner(input);
			if(command.hasNext()){
				for(BotCommand b:BotCommands.commandList){
					if(input.indexOf(b.node.replace("command.","").replace("#all",""))==0&&!input.contains("verify")){
						b.run(e.getMessage());
					}
				}
			}
			command.close();
		}
	}

}
