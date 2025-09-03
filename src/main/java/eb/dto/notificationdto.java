/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package eb.dto;

/**
 *
 * @author ACER
 */
public class notificationdto {

    private String notification_id;
    private String reciever_id;
    private String transfer_id;
    private String channel;
    private String email;
    private String phone;
    private String ticket_id;
    private String subject;
    private String message_body;
    private String status;

    public notificationdto(String notification_id, String reciever_id, String transfer_id, String channel, String email, String phone, String ticket_id, String subject, String message_body, String status) {
        this.notification_id = notification_id;
        this.reciever_id = reciever_id;
        this.transfer_id = transfer_id;
        this.channel = channel;
        this.email = email;
        this.phone = phone;
        this.ticket_id = ticket_id;
        this.subject = subject;
        this.message_body = message_body;
        this.status = status;
    }

    public notificationdto() {
    }

    public String getNotification_id() {
        return notification_id;
    }

    public void setNotification_id(String notification_id) {
        this.notification_id = notification_id;
    }

    public String getReciever_id() {
        return reciever_id;
    }

    public void setReciever_id(String reciever_id) {
        this.reciever_id = reciever_id;
    }

    public String getTransfer_id() {
        return transfer_id;
    }

    public void setTransfer_id(String transfer_id) {
        this.transfer_id = transfer_id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getTicket_id() {
        return ticket_id;
    }

    public void setTicket_id(String ticket_id) {
        this.ticket_id = ticket_id;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage_body() {
        return message_body;
    }

    public void setMessage_body(String message_body) {
        this.message_body = message_body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}
