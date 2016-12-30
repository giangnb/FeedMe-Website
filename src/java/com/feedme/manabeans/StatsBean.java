/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.feedme.manabeans;

import com.feedme.global.GlobalBean;
import com.feedme.service.Employee;
import com.feedme.service.EmployeeDTO;
import com.feedme.service.OrderDetail;
import com.feedme.ws.Methods;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import org.primefaces.model.chart.Axis;
import org.primefaces.model.chart.AxisType;
import org.primefaces.model.chart.BarChartModel;
import org.primefaces.model.chart.BarChartSeries;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.DateAxis;

/**
 *
 * @author Giang
 */
@ManagedBean(name = "statsBean")
@ViewScoped
public class StatsBean implements Serializable {

    private List<OrderDetail> orders;
    private Date from, to;
    private BarChartModel barModel;

    /**
     * Creates a new instance of StatisticsBean
     */
    public StatsBean() {
        to = new Date();
        from = new Date(to.getTime() - 1000 * 60 * 60 * 24);
        doGetOrderDetail();
    }

    public List<OrderDetail> getOrders() {
        return orders;
    }

    public void setOrders(List<OrderDetail> orders) {
        this.orders = orders;
    }

    public Date getFrom() {
        return from;
    }

    public void setFrom(Date from) {
        this.from = from;
    }

    public Date getTo() {
        return to;
    }

    public void setTo(Date to) {
        this.to = to;
    }

    public BarChartModel getBarModel() {
        return barModel;
    }

    public void setBarModel(BarChartModel barModel) {
        this.barModel = barModel;
    }

    public void doGetOrderDetail() {
        if (from == null || to == null) {
            return;
        }
        long fromTime, toTime;
        fromTime = from.getTime();
        toTime = to.getTime();
        if (fromTime >= toTime) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi nhập liệu", "Ngày bắt đầu phải nhỏ hơn ngày kết thúc"));
        }
        if (selectedEmployee == null) {
            orders = Methods.fetchOrders(fromTime + "", toTime + "");
        } else {
            orders = Methods.fetchOrdersByEmployee(fromTime + "", toTime + "", selectedEmployee.getId());
        }
        initBarModel(from, to);
    }

    public int doCountOrders(long f, long t) {
        return orders.stream().filter(o -> {
            return o.getOrdertime().compareTo(f + "") >= 0 && o.getOrdertime().compareTo(t + "") < 0;
        }).collect(Collectors.toList()).size();
    }

    public int doGetCountOrders() {
        return doCountOrders(from.getTime(), to.getTime());
    }

    public double doSumOrdersValue(long f, long t) {
        List<OrderDetail> list = orders.stream().filter(o -> {
            return o.getOrdertime().compareTo(f + "") >= 0 && o.getOrdertime().compareTo(t + "") < 0;
        }).collect(Collectors.toList());
        double sum = 0d;
        for (OrderDetail od : list) {
            sum += od.getSubtotal() - od.getDiscount();
        }
        return sum;
    }

    public double doSumOrdersDiscountValue(long f, long t) {
        List<OrderDetail> list = orders.stream().filter(o -> {
            return o.getOrdertime().compareTo(f + "") >= 0 && o.getOrdertime().compareTo(t + "") < 0;
        }).collect(Collectors.toList());
        double sum = 0d;
        for (OrderDetail od : list) {
            sum += od.getDiscount();
        }
        return sum;
    }

    private void initBarModel(Date f, Date t) {
        // Round the date (set min and sec to zero)
        SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd HH");
        String fromDate = fmt.format(f) + ":00:00";
        String toDate = fmt.format(t) + ":00:00";
        fmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            f = fmt.parse(fromDate);
            t = fmt.parse(toDate);
        } catch (ParseException ex) {
            FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_ERROR, "Lỗi", "Có vấn đề xảy ra khi tổng hợp dữ liệu"));
            //ex.printStackTrace();
            return;
        }
        fmt = new SimpleDateFormat(GlobalBean.getPropertyValue("format_date") + " " + GlobalBean.getPropertyValue("format_time"));
        // Count step length
        long length = t.getTime() - f.getTime();
        long step;
        if (length < 60 * 60 * 1000L) { // Less than an hour
            step = 15 * 60 * 1000; // Each 10 mins
        } else if (length < 60 * 60 * 12 * 1000L) { // Less than 12hrs
            step = 30 * 60 * 1000; // Each 30 mins
        } else if (length < 60 * 60 * 24 * 1000L) { // Less than a day
            step = 60 * 60 * 1000; // Each hour
        } else if (length <= 60 * 60 * 24 * 5 * 1000L) { // Less than 5 days
            step = 60 * 60 * 12 * 1000; // Each 12 hrs
        } else { // More tan 5 days
            step = 60 * 60 * 24 * 1000; // Each day
        }
        // Start to create chart
        int count = 0;
        barModel = new BarChartModel();
        ChartSeries s1 = new ChartSeries("Giá trị");
        ChartSeries s2 = new ChartSeries("Giảm giá");
        double maxAxis = 0D;
        double sum;
        while (t.getTime() >= f.getTime()) {
            sum = doSumOrdersValue(f.getTime(), f.getTime() + step);
            s1.set(fmt.format(f), sum);
            if (maxAxis < sum) {
                maxAxis = sum;
            }
            sum = doSumOrdersDiscountValue(f.getTime(), f.getTime() + step);
            s2.set(fmt.format(f), sum);
            if (maxAxis < sum) {
                maxAxis = sum;
            }
            f = new Date(f.getTime() + step);
            count++;
            // Saving server's resource
            if (count >= 30) {
                FacesContext.getCurrentInstance().addMessage("", new FacesMessage(FacesMessage.SEVERITY_WARN, "Dữ liệu quá lớn", "Chỉ một phần được hiển thị do khối lượng dữ liệu xử lý quá lớn"));
                break;
            }
        }
        barModel.addSeries(s1);
        barModel.addSeries(s2);
        barModel.setLegendPosition("ne");

        Axis xAxis = barModel.getAxis(AxisType.X);
        xAxis.setLabel("Giá trị");
        xAxis.setTickAngle(-50);

        Axis yAxis = barModel.getAxis(AxisType.Y);
        yAxis.setLabel("Thời gian");
        yAxis.setMin(0);
        yAxis.setMax(maxAxis + 10);
    }

    /* EMPLOYEE SELECTION */
    private List<Employee> employees;
    private Employee selectedEmployee;

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public Employee getSelectedEmployee() {
        return selectedEmployee;
    }

    public void setSelectedEmployee(Employee selectedEmployee) {
        this.selectedEmployee = selectedEmployee;
    }

    public void doLoadEmployees() {
        if (employees == null) {
            employees = Methods.fetchEmployees().stream().map(EmployeeDTO::getEmployee).collect(Collectors.toList());
        }
    }

    public void doSelectEmployee(Employee e) {
        selectedEmployee = e;
        doGetOrderDetail();
    }
}
