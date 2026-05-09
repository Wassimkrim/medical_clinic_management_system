# مشروع إدارة عيادة طبية - النسخة الإنجليزية

هذه نسخة جديدة بالإنجليزية من برنامج إدارة العيادة الطبية.
الواجهات، أسماء الجداول، الرسائل، الأدوار، والتقرير كلها بالإنجليزية.

## التشغيل

1. شغل XAMPP ثم MySQL.
2. افتح phpMyAdmin.
3. نفذ ملف قاعدة البيانات:
   create_database.sql
4. افتح المشروع في Eclipse:
   File > Import > Maven > Existing Maven Projects
5. اختر مجلد المشروع:
   medical-clinic-management-english
6. شغل الملف:
   src/main/java/com/clinicmanagement/App.java

## حسابات الدخول

Admin:
username: admin
password: admin123

Doctor:
username: doctor
password: doc123

Secretary:
username: secretary
password: sec123

## ملاحظة مهمة

قاعدة البيانات الجديدة اسمها:
medical_clinic

لذلك يجب تنفيذ create_database.sql قبل تشغيل البرنامج.
