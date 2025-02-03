package com.xatkit.example;

import com.xatkit.core.XatkitBot;
import com.xatkit.plugins.react.platform.ReactPlatform;
import com.xatkit.plugins.react.platform.io.ReactEventProvider;
import com.xatkit.plugins.react.platform.io.ReactIntentProvider;
import lombok.val;
import org.apache.commons.configuration2.BaseConfiguration;
import org.apache.commons.configuration2.Configuration;

import static com.xatkit.dsl.DSL.eventIs;
import static com.xatkit.dsl.DSL.fallbackState;
import static com.xatkit.dsl.DSL.intent;
import static com.xatkit.dsl.DSL.intentIs;
import static com.xatkit.dsl.DSL.model;
import static com.xatkit.dsl.DSL.state;

public class GreetingsBot {

    public static void main(String[] args) {

        // Define multiple intents for various greetings and general questions
        val greetings = intent("Greetings")
                .trainingSentence("Hi")
                .trainingSentence("Hello")
                .trainingSentence("Good morning")
                .trainingSentence("Good afternoon");

        val howAreYou = intent("HowAreYou")
                .trainingSentence("How are you?")
                .trainingSentence("What's up?")
                .trainingSentence("How do you feel?");

        // Adding additional intents for other general questions
        val nameIntent = intent("Name")
                .trainingSentence("What is your name?")
                .trainingSentence("Who are you?")
                .trainingSentence("What should I call you?");
        
        val ageIntent = intent("Age")
                .trainingSentence("How old are you?")
                .trainingSentence("What is your age?")
                .trainingSentence("How many years have you lived?");
        
        val ownerIntent = intent("Owner")
                .trainingSentence("Who is your owner?")
                .trainingSentence("Do you have an owner?")
                .trainingSentence("Who created you?");
        
        val hobbyIntent = intent("Hobby")
                .trainingSentence("What are your hobbies?")
                .trainingSentence("What do you like to do?")
                .trainingSentence("Do you have any hobbies?");

        // More intents (Repeat this structure for other intents)
        val weatherIntent = intent("Weather")
                .trainingSentence("What is the weather like today?")
                .trainingSentence("Is it sunny outside?")
                .trainingSentence("What is the temperature?");
        
        val timeIntent = intent("Time")
                .trainingSentence("What time is it?")
                .trainingSentence("Can you tell me the time?")
                .trainingSentence("What’s the current time?");
        
        // Instantiate the platform we will use in the bot definition.
        ReactPlatform reactPlatform = new ReactPlatform();
        ReactEventProvider reactEventProvider = reactPlatform.getReactEventProvider();
        ReactIntentProvider reactIntentProvider = reactPlatform.getReactIntentProvider();

        // Define states
        val init = state("Init");
        val awaitingInput = state("AwaitingInput");
        val handleWelcome = state("HandleWelcome");
        val handleWhatsUp = state("HandleWhatsUp");
        val handleName = state("HandleName");
        val handleAge = state("HandleAge");
        val handleOwner = state("HandleOwner");
        val handleHobby = state("HandleHobby");
        val handleWeather = state("HandleWeather");
        val handleTime = state("HandleTime");

        // Define transitions between states
        init
                .next()
                .when(eventIs(ReactEventProvider.ClientReady)).moveTo(awaitingInput);

        awaitingInput
                .next()
                .when(intentIs(greetings)).moveTo(handleWelcome)
                .when(intentIs(howAreYou)).moveTo(handleWhatsUp)
                .when(intentIs(nameIntent)).moveTo(handleName)
                .when(intentIs(ageIntent)).moveTo(handleAge)
                .when(intentIs(ownerIntent)).moveTo(handleOwner)
                .when(intentIs(hobbyIntent)).moveTo(handleHobby)
                .when(intentIs(weatherIntent)).moveTo(handleWeather)
                .when(intentIs(timeIntent)).moveTo(handleTime);

        handleWelcome
                .body(context -> reactPlatform.reply(context, "Hi, nice to meet you!"))
                .next()
                .moveTo(awaitingInput);

        handleWhatsUp
                .body(context -> reactPlatform.reply(context, "I am fine and you?"))
                .next()
                .moveTo(awaitingInput);

        handleName
                .body(context -> reactPlatform.reply(context, "My name is XatkitBot. Nice to meet you!"))
                .next()
                .moveTo(awaitingInput);

        handleAge
                .body(context -> reactPlatform.reply(context, "I am timeless, but I was created recently!"))
                .next()
                .moveTo(awaitingInput);

        handleOwner
                .body(context -> reactPlatform.reply(context, "I was created by a developer using Xatkit!"))
                .next()
                .moveTo(awaitingInput);

        handleHobby
                .body(context -> reactPlatform.reply(context, "I enjoy chatting and helping people!"))
                .next()
                .moveTo(awaitingInput);

        handleWeather
                .body(context -> reactPlatform.reply(context, "Sorry, I can't check the weather, but you can look it up!"))
                .next()
                .moveTo(awaitingInput);

        handleTime
                .body(context -> reactPlatform.reply(context, "I don't have a watch, but you can check your device!"))
                .next()
                .moveTo(awaitingInput);

        // Default fallback
        val defaultFallback = fallbackState()
                .body(context -> reactPlatform.reply(context, "Sorry, I didn't get that"));

        // Define the bot model
        val botModel = model()
                .usePlatform(reactPlatform)
                .listenTo(reactEventProvider)
                .listenTo(reactIntentProvider)
                .initState(init)
                .defaultFallbackState(defaultFallback);

        Configuration botConfiguration = new BaseConfiguration();
        XatkitBot xatkitBot = new XatkitBot(botModel, botConfiguration);
        xatkitBot.run();
    }
}
