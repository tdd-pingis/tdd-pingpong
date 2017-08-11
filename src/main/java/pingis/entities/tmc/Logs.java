/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pingis.entities.tmc;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import javax.persistence.Transient;

/**
 *
 * @author authority
 */
@Entity
public class Logs {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @OneToOne
    private TestOutput testOutput;

    @Transient
    private byte[] stdout;
    @Transient
    private byte[] stderr;
    
    @Lob
    private String stdoutString;
    @Lob
    private String stderrString;

    public byte[] getStdout() {
        return stdout;
    }

    public String getStdoutString() {
        return stdoutString;
    }

    public void setStdout(byte[] stdout) {
        this.stdout = stdout;
        this.stdoutString = new String(stdout);
    }

    public byte[] getStderr() {
        return stderr;
    }
    
    public String getStderrString() {
        return stderrString;
    }

    public void setStderr(byte[] stderr) {
        this.stderr = stderr;
        this.stderrString = new String(stderr);
    }
}
