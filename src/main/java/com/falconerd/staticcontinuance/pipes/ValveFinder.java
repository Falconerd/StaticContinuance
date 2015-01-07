package com.falconerd.staticcontinuance.pipes;

import com.falconerd.staticcontinuance.utility.LogHelper;

import java.util.*;

public class ValveFinder
{
    public static void updateNetwork(TileEntityPipe pipe)
    {
        // Check to make sure we are on output mode.

        // Check if we have valid connections.
        if (pipe.hasValidConnections())
        {

            List<TileEntityPipe> valves = getConnectedValves(pipe);
            for (TileEntityPipe pipe1 : valves)
            {
                LogHelper.info("Pipe found at: " + pipe1.getPos());
            }

        }
    }

    /**
     * This method finds all valves in the network connected to this pipe
     *
     * @param pipe The pipe which we connected
     * @return A list of valves
     */
    public static List<TileEntityPipe> getConnectedValves(TileEntityPipe pipe)
    {
        // This is the list of valves we need to find!
        List<TileEntityPipe> valves = new ArrayList<TileEntityPipe>();

        // This is the queue which we add to and search for new pipes from
        Queue<TileEntityPipe> frontier = new LinkedList<TileEntityPipe>();
        frontier.add(pipe);

        // This is a list of visited places
        HashMap<TileEntityPipe, Boolean> visited = new HashMap<TileEntityPipe, Boolean>();
        visited.put(pipe, true);

        while (!frontier.isEmpty())
        {
            /**
             * Queue.poll() retrieves and removes the head of the queue or returns null
             * if the queue is empty - which it shouldn't be; due to the above while condition.
             */
            TileEntityPipe current = frontier.poll();
            if (current != null)
            {
                // Add this place to the list of places we have visited
                visited.put(current, true);

                // Check to see if this is a valve
                if (current.isValve())
                {
                    // Add it to the list
                    valves.add(current);
                }

                // Search for new pipes to add to the queue
                for (TileEntityPipe next : current.getValidConnections())
                {
                    // If we haven't been there before...
                    if (!visited.containsKey(next))
                    {
                        // Add it to the list of places to visit
                        frontier.add(next);
                    }
                }
            }
        }

        LogHelper.info("Visited this many places: " + visited.size());

        for (TileEntityPipe pipes : visited.keySet())
        {
            LogHelper.info(pipes.getPos());
        }

        for (TileEntityPipe valve : valves)
        {
            LogHelper.info("Valve @ : " + valve.getPos());
        }

        return valves;
    }
}
