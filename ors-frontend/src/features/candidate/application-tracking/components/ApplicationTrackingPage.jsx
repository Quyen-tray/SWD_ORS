import React, { useState, useEffect } from 'react';
import { applicationTrackingApi } from '../api/applicationTrackingApi';

export function ApplicationTrackingPage({ candidateId = 1 }) { // TODO: Get candidateId from actual Auth context
    const [stats, setStats] = useState({
        totalApplications: 0,
        pendingInterviews: 0,
        savedJobs: 0,
        profileCompletionPercentage: 0
    });
    const [applications, setApplications] = useState([]);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        fetchData();
    }, [candidateId]);

    const fetchData = async () => {
        setLoading(true);
        try {
            const [statsData, appsData] = await Promise.all([
                applicationTrackingApi.getDashboardStats(candidateId),
                applicationTrackingApi.getApplications(candidateId)
            ]);
            setStats(statsData);
            setApplications(appsData);
        } catch (error) {
            console.error("Lỗi khi tải dữ liệu dashboard:", error);
        } finally {
            setLoading(false);
        }
    };

    const handleWithdraw = async (applicationId) => {
        if (window.confirm("Bạn có chắc chắn muốn rút đơn? Hành động này không thể hoàn tác (BR-17).")) {
            try {
                await applicationTrackingApi.withdrawApplication(applicationId);
                alert("Đã rút đơn thành công!");
                fetchData(); // Refresh list & stats
            } catch (error) {
                alert(error.response?.data?.message || "Không thể rút đơn lúc này.");
            }
        }
    };

    if (loading) return <div>Đang tải dữ liệu dashboard...</div>;

    return (
        <div className="space-y-8">
            {/* UC-73: Dashboard */}
            <div className="space-y-6">
                <div className="bg-gradient-to-r from-indigo-600 to-blue-500 rounded-2xl p-6 text-white shadow-sm">
                    <h1 className="text-2xl font-bold">Candidate Dashboard (UC-73)</h1>
                    <p className="text-indigo-100 text-sm mt-1">Tổng quan hoạt động tuyển dụng của bạn.</p>
                </div>

                <div className="grid grid-cols-1 md:grid-cols-4 gap-4">
                    <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                        <div>
                            <p className="text-xs text-slate-500 font-semibold uppercase">Việc đã ứng tuyển</p>
                            <p className="text-2xl font-bold text-slate-800 mt-1">{stats.totalApplications}</p>
                        </div>
                    </div>
                    <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                        <div>
                            <p className="text-xs text-slate-500 font-semibold uppercase">Lịch phỏng vấn</p>
                            <p className="text-2xl font-bold text-amber-600 mt-1">{stats.pendingInterviews}</p>
                        </div>
                    </div>
                    <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                        <div>
                            <p className="text-xs text-slate-500 font-semibold uppercase">Việc làm đã lưu</p>
                            <p className="text-2xl font-bold text-indigo-600 mt-1">{stats.savedJobs}</p>
                        </div>
                    </div>
                    <div className="bg-white p-5 rounded-xl border border-slate-200 shadow-sm flex items-center justify-between">
                        <div>
                            <p className="text-xs text-slate-500 font-semibold uppercase">Độ hoàn thiện</p>
                            <p className="text-2xl font-bold text-green-600 mt-1">{stats.profileCompletionPercentage}%</p>
                        </div>
                    </div>
                </div>
            </div>

            {/* UC-71: Application History */}
            <div className="bg-white p-6 rounded-xl border border-slate-200 shadow-sm">
                <h2 className="text-xl font-bold text-slate-800 mb-4">Lịch sử ứng tuyển (UC-71)</h2>
                <div className="overflow-x-auto">
                    <table className="w-full text-left border-collapse">
                        <thead>
                            <tr className="border-b bg-slate-50 text-slate-500 text-sm">
                                <th className="p-3">Mã Job</th>
                                <th className="p-3">Ngày nộp</th>
                                <th className="p-3">Trạng thái</th>
                                <th className="p-3 text-right">Hành động</th>
                            </tr>
                        </thead>
                        <tbody>
                            {applications.length === 0 ? (
                                <tr>
                                    <td colSpan="4" className="text-center p-4 text-slate-500">Chưa có lịch sử ứng tuyển.</td>
                                </tr>
                            ) : applications.map(app => (
                                <tr key={app.id} className="border-b hover:bg-slate-50">
                                    <td className="p-3 font-medium text-indigo-600">JOB-{app.jobPost?.id || 'N/A'}</td>
                                    <td className="p-3 text-sm">{app.appliedAt ? new Date(app.appliedAt).toLocaleDateString('vi-VN') : ''}</td>
                                    <td className="p-3">
                                        <span className={`px-2 py-1 text-xs font-bold rounded-full 
                                            ${app.status === 'WITHDRAWN' ? 'bg-slate-200 text-slate-700' :
                                                app.status === 'INTERVIEW_SCHEDULED' ? 'bg-amber-100 text-amber-700' : 
                                                app.status === 'REJECTED' ? 'bg-red-100 text-red-700' :
                                                app.status === 'HIRED' ? 'bg-green-100 text-green-700' : 'bg-blue-100 text-blue-700'}`}>
                                            {app.status}
                                        </span>
                                    </td>
                                    <td className="p-3 text-right">
                                        <button
                                            onClick={() => handleWithdraw(app.id)}
                                            disabled={app.status === 'WITHDRAWN' || app.status === 'HIRED' || app.status === 'REJECTED'}
                                            className="text-red-500 hover:text-red-700 text-sm font-semibold disabled:text-slate-300">
                                            Rút đơn
                                        </button>
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                </div>
            </div>
            
            <div className="border-t pt-6 mt-8">
                 <h2 className="text-xl font-bold text-slate-800 mb-4">Quản lý Thông báo (UC-72 - TODO)</h2>
                 <p className="text-slate-500">Phần settings email/sms alerts và real-time feed sẽ tích hợp tại đây.</p>
            </div>
        </div>
    );
}
