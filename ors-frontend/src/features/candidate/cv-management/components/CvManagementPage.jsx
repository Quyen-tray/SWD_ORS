import React, { useState, useEffect } from 'react';
import { cvManagementApi } from '../api/cvManagementApi';

export function CvManagementPage({ userId = 1 }) { // TODO: Get userId from actual Auth context
    const [profile, setProfile] = useState({ fullName: '', phoneNumber: '', avatarUrl: '' });
    const [isEditing, setIsEditing] = useState(false);
    const [loading, setLoading] = useState(true);

    useEffect(() => {
        cvManagementApi.getProfile(userId)
            .then(data => {
                setProfile(data);
                setLoading(false);
            })
            .catch(err => {
                console.error(err);
                setLoading(false);
            });
    }, [userId]);

    const handleChange = (e) => {
        setProfile({ ...profile, [e.target.name]: e.target.value });
    };

    const handleSave = async (e) => {
        e.preventDefault();
        try {
            await cvManagementApi.updateProfile(userId, profile);
            alert("Cập nhật thông tin thành công!");
            setIsEditing(false);
        } catch (error) {
            alert("Lỗi khi cập nhật thông tin!");
        }
    };

    if (loading) return <div>Đang tải hồ sơ...</div>;

    return (
        <div className="max-w-2xl mx-auto p-6 bg-white rounded-xl shadow-sm border border-slate-200 mt-6">
            <h2 className="text-xl font-bold text-slate-800 mb-4">Hồ sơ cá nhân (UC-66)</h2>

            {isEditing ? (
                <form onSubmit={handleSave} className="space-y-4">
                    <div>
                        <label className="block text-sm font-semibold text-slate-700">Họ và tên</label>
                        <input type="text" name="fullName" value={profile.fullName || ''} onChange={handleChange}
                            className="mt-1 w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500" required />
                    </div>
                    <div>
                        <label className="block text-sm font-semibold text-slate-700">Số điện thoại</label>
                        <input type="tel" name="phoneNumber" value={profile.phoneNumber || ''} onChange={handleChange}
                            className="mt-1 w-full px-3 py-2 border rounded-lg focus:ring-2 focus:ring-indigo-500" />
                    </div>
                    {/* TODO: Add Avatar Upload for UC-66 */}
                    <div className="flex space-x-3 pt-4">
                        <button type="button" onClick={() => setIsEditing(false)}
                            className="px-4 py-2 border rounded-lg hover:bg-slate-50">Hủy</button>
                        <button type="submit"
                            className="px-4 py-2 bg-indigo-600 text-white rounded-lg hover:bg-indigo-700">Lưu thay đổi</button>
                    </div>
                </form>
            ) : (
                <div className="space-y-4">
                    <div className="flex justify-between border-b pb-2">
                        <span className="text-slate-500">Họ và tên:</span>
                        <span className="font-semibold text-slate-800">{profile.fullName || 'Chưa cập nhật'}</span>
                    </div>
                    <div className="flex justify-between border-b pb-2">
                        <span className="text-slate-500">Số điện thoại:</span>
                        <span className="font-semibold text-slate-800">{profile.phoneNumber || 'Chưa cập nhật'}</span>
                    </div>
                    <button onClick={() => setIsEditing(true)}
                        className="mt-4 w-full px-4 py-2 border border-indigo-600 text-indigo-600 rounded-lg font-semibold hover:bg-indigo-50">
                        Chỉnh sửa hồ sơ
                    </button>
                </div>
            )}
            
            <div className="mt-8 border-t pt-6">
                <h2 className="text-xl font-bold text-slate-800 mb-4">Quản lý CV (UC-67 - TODO)</h2>
                <p className="text-slate-500">Phần quản lý CV (Upload PDF, CV Builder) sẽ được tích hợp tại đây.</p>
            </div>
        </div>
    );
}
