# Git hook for commit message

## What the script do ?

This script allow you to avoid writing the ticket number in your commit message.
It automatically add it before your commit message if you respect rules described on [contributing](../.github/CONTRIBUTING.md)

Indeed, if you're working on a fix ticket or a feature ticket, you should use this hook!

## How it works ?

To allow the script to fill your commit message, you must use this keyword at the beginning of your commit message "$ "

###Example : 

You're on a branch called : TICKET-440/new-feature or fix/TICKET-440/bug-fix

    $ git commit -m "$ Update Changelog"

The commit message will become "[TICKET-440] Update Changelog"

## How to use it ?

In the root folder of your repository :
    
    $ cd .git/hooks/
    $ mv prepare-commit-msg.sample prepare-commit-msg
    $ vim prepare-commit-msg

And write this snippet on it:

    #!/bin/bash
    
    REAL_BRANCH_NAME=$(git symbolic-ref --short HEAD)
    BRANCH_NAME=$(echo "$REAL_BRANCH_NAME" | cut -d/ -f1)
    
    if [ "$BRANCH_NAME" == "fix" ]; then
        BRANCH_NAME=$(echo "$REAL_BRANCH_NAME" | cut -d/ -f2)
    fi
    
    CURRENT_COMMIT_MESSAGE=$(cat $1)
    FIRST_CHARACTERS="$(echo $CURRENT_COMMIT_MESSAGE | head -c 2)"
    CURRENT_COMMIT_MESSAGE=$(echo $CURRENT_COMMIT_MESSAGE | cut -c 3-)
    
    if [ -n "$BRANCH_NAME" ] && [ "$FIRST_CHARACTERS" == '$ ' ]; then
        echo "[$BRANCH_NAME] $CURRENT_COMMIT_MESSAGE" > $1
    fi
    
Then you should give the file executable using a ```$ chmod +x prepare-commit-msg```