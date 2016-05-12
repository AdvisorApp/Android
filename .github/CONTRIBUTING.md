# Contributing

## Commit and PR message syntax

Check that each commit related to a JIRA ticket should start with the JIRA ticket code (example: "[ADV-42] ...")

Same for your PR name

## Branch naming

The branch you are working on should be named properly to be easily understandable by another developer.

### For a fix bug

For a fix bug the branch should be created from the `master` branch and should be named according to the following pattern:

    fix/ADV-[0-9]+/[a-z\-]+

example:

    fix/ADV-208/delete-comments

This example is for a fix bug ticket which identifier on JIRA is ADV-208 and which is related to fixing comment
deletion.

### For a task/feature

For other tickets such as tasks or features the corresponding branch should be created from the `develop` branch and
named according to the following pattern:

    ADV-[0-9]+/[a-z\-]+

example:

    ADV-165/pagination-icons

This example is for a simple task ticket whose reference is ADV-165 and which replaces pagination arrows by actual
icons.

### Git hook

If you don't want to enter your ticket number on each commit you write, you can use a git hook which is describe on this [page](../Resources/pre-commit-message.md)

# Development Guidelines

In order to add new code into the project you should respect the following:
 1. CodeStyle
