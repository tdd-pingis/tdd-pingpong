/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.utils;

/**
 *
 * @author lauri
 */
public class ChallengeImplementationData {
    public String name;
    public int totalTasks;
    public int completedTasks;
    public float completedRatio;
    public int currentTaskImplId;
    
    public ChallengeImplementationData(String name,
            int totalTasks, 
            int completedTasks,
            float completedRatio, 
            int currentTaskImpId) {
        this.name = name;
        this.totalTasks = totalTasks;
        this.completedTasks = completedTasks;
        this.completedRatio = completedRatio;
        this.currentTaskImplId = currentTaskImpId;
    }
}
