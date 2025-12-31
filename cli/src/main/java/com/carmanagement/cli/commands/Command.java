package com.carmanagement.cli.commands;

import com.carmanagement.cli.http.ApiClient;

public interface Command {
    
    String getName();

    String getDescription();

    String getUsage();

    void execute(ApiClient client, String[] args) throws Exception;
}

