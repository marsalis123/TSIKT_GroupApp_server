package org.example.test_1_server;

import org.springframework.stereotype.Service;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {

    // addJob
    public boolean addJob(JobDto job) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "INSERT INTO jobs " +
                    "(title, description, group_id, status, created_by, assigned_to) " +
                    "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, job.title);
            pstmt.setString(2, job.description);
            pstmt.setInt(3, job.groupId);
            pstmt.setString(4, job.status);
            pstmt.setInt(5, job.createdBy);
            pstmt.setInt(6, job.assignedTo);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // updateJob
    public boolean updateJob(int jobId, JobDto job) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "UPDATE jobs SET title = ?, description = ?, group_id = ?, " +
                    "status = ?, assigned_to = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, job.title);
            pstmt.setString(2, job.description);
            pstmt.setInt(3, job.groupId);
            pstmt.setString(4, job.status);
            pstmt.setInt(5, job.assignedTo);
            pstmt.setInt(6, jobId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // getUserJobs – rovnaká logika ako v tvojom UserManager
    public List<JobDto> getUserJobs(int userId) {
        List<JobDto> jobs = new ArrayList<>();
        try (Connection conn = DBManager.getConnection()) {

            String sqlGroups = "SELECT g.id FROM groups g " +
                    "LEFT JOIN group_members gm ON g.id = gm.group_id " +
                    "WHERE g.owner = (SELECT username FROM users WHERE id = ?) OR gm.user_id = ?";
            PreparedStatement stmtGroups = conn.prepareStatement(sqlGroups);
            stmtGroups.setInt(1, userId);
            stmtGroups.setInt(2, userId);
            ResultSet rsGroups = stmtGroups.executeQuery();
            List<Integer> groupIds = new ArrayList<>();
            while (rsGroups.next()) groupIds.add(rsGroups.getInt(1));
            if (groupIds.isEmpty()) return jobs;

            String inClause = groupIds.stream()
                    .map(String::valueOf)
                    .collect(Collectors.joining(","));

            String sqlJobs = "SELECT * FROM jobs WHERE group_id IN (" + inClause + ")";
            PreparedStatement stmtJobs = conn.prepareStatement(sqlJobs);
            ResultSet rsJobs = stmtJobs.executeQuery();
            while (rsJobs.next()) {
                jobs.add(new JobDto(
                        rsJobs.getInt("id"),
                        rsJobs.getString("title"),
                        rsJobs.getString("description"),
                        rsJobs.getInt("group_id"),
                        rsJobs.getString("status"),
                        rsJobs.getInt("created_by"),
                        rsJobs.getInt("assigned_to"),
                        rsJobs.getString("created_at")
                ));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jobs;
    }

    // addJobLog
    public boolean addJobLog(JobLogDto log) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "INSERT INTO job_logs " +
                    "(job_id, user_id, work_text, commit_msg, pdf_path) " +
                    "VALUES (?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, log.jobId);
            pstmt.setInt(2, log.userId);
            pstmt.setString(3, log.workText);
            pstmt.setString(4, log.commitMsg);
            pstmt.setString(5, log.pdfPath);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // getJobLogs
    public List<JobLogDto> getJobLogs(int jobId) {
        List<JobLogDto> list = new ArrayList<>();
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT l.*, u.name as authorName FROM job_logs l " +
                    "JOIN users u ON l.user_id = u.id " +
                    "WHERE l.job_id = ? ORDER BY l.created_at ASC";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, jobId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                list.add(new JobLogDto(
                        rs.getInt("id"),
                        rs.getInt("job_id"),
                        rs.getInt("user_id"),
                        rs.getString("work_text"),
                        rs.getString("commit_msg"),
                        rs.getString("pdf_path"),
                        rs.getString("created_at"),
                        rs.getString("authorName")
                ));
            }


        } catch (SQLException e) {
            e.printStackTrace();
        }


        return list;
    }

    public boolean updateJobStatus(int jobId, String newStatus) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "UPDATE jobs SET status = ? WHERE id = ?";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, newStatus);
            pstmt.setInt(2, jobId);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public Integer getJobGroupId(int jobId) {
        try (Connection conn = DBManager.getConnection()) {
            String sql = "SELECT group_id FROM jobs WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, jobId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getInt("group_id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


}
